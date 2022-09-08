from unittest import TestCase
from django.shortcuts import render
from django.core.exceptions import ObjectDoesNotExist
from rest_framework import viewsets
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .serializers import MatchingSerializer, VoteSerializer, VoteProfileSerializer
from .models import MatchingRoom
from userprofile.models import UserProfile

import json
import hashlib


# Create your views here.
class MatchingRoomViewSet(viewsets.ViewSet):
    permission_classes_by_action = {'list': [IsAuthenticated],
                                    'create': [IsAuthenticated]}

    def list(self, request, **kwargs):
        userId = request.user.username
        user = UserProfile.objects.get(userid=userId)
        queryset = MatchingRoom.objects.filter(userInfo=user)
        serializer = MatchingSerializer(queryset, many=True)
        return Response(serializer.data, status=200)

    def create(self, request, **kwargs):
        requestData = request.data
        userId = request.user.username
        try:
            userInfo = UserProfile.objects.get(userid=userId)
        except ObjectDoesNotExist:
            return Response({"error": "can't find user information from given credentials."}, status=400)
        roomInfo = requestData["city"] + requestData["county"] + requestData["district"] + requestData["game"]
        roomInfo = hashlib.sha1(roomInfo.encode('utf-8')).hexdigest()

        try:
            matchInExist = MatchingRoom.objects.get(matchId=roomInfo)
            matchInExist.userInfo.add(userInfo)
            test = MatchingRoom.objects.filter(matchId=roomInfo)
            serializer = MatchingSerializer(test, many=True)
            return Response({"matchId": roomInfo}, status=200)
        except ObjectDoesNotExist:
            requestData = {"userInfo": userInfo.__dict__,
                        "city": requestData["city"],
                        "county": requestData["county"],
                        "district": requestData["district"],
                        "game": requestData["game"],
                        "matchId": roomInfo}
            serializer = MatchingSerializer(data=requestData)
            if serializer.is_valid():
                matchroom = serializer.save()
                matchroom.userInfo.add(userInfo)
                return Response({"matchId": roomInfo}, status=200)
            return Response(serializer.errors, status=400)

    def get_permissions(self):
        try:
            return [permission() for permission
                    in self.permission_classes_by_action[self.action]]
        except KeyError:
            return [permission() for permission in self.permission_classes]

class MatchingDetailedInfo(viewsets.ViewSet):
    def list(self, request, **kwargs):
        matchid = request.GET.get("matchId", None)

        match = MatchingRoom.objects.filter(matchId=matchid)
        serializer = MatchingSerializer(match, many=True)
        return Response(serializer.data, status=200)

class MatchingRoomVoteViewSet(viewsets.ViewSet):
    def create(self, request, **kwargs):
        userdata = UserProfile.objects.get(userid=request.user.username)
        data = request.data

        requestData = {"voteTitle": data["voteTitle"],
                       "matchId": data["matchId"],
                       "startTime": data["startTime"],
                       "endTime": data["endTime"],
                       "date": data["date"],
                       "content": data["content"]} 

        serializer = VoteSerializer(data=requestData)
        if serializer.is_valid():
            savedVote = serializer.save()
            userinfodata = {"vote": savedVote.id,
                            "name": userdata.name,
                            "like": 0}
            voteserializer = VoteProfileSerializer(data = userinfodata)
            if(voteserializer.is_valid()):
                voteserializer.save()
            else:
                return Response(voteserializer.errors)
            match = MatchingRoom.objects.get(matchId=data["matchId"])
            match.voteInfo.add(savedVote)
            return Response(serializer.data, status=200)
        return Response(serializer.errors, status=400)
        