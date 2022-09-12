from unittest import TestCase
from django.shortcuts import render
from django.core.exceptions import ObjectDoesNotExist
from rest_framework import viewsets
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .serializers import MatchingSerializer, VoteSerializer, VoteProfileSerializer, SimplifiedMatchingSerializer
from .models import MatchingRoom, Vote
from userprofile.models import UserProfile

from .matchAlgorithm import findNearbyLocation

import json
import hashlib
import uuid


# Create your views here.
class MatchingRoomViewSet(viewsets.ViewSet):
    permission_classes_by_action = {'list': [IsAuthenticated],
                                    'create': [IsAuthenticated]}

    def list(self, request, **kwargs):
        userId = request.user.username
        user = UserProfile.objects.get(userid=userId)
        queryset = MatchingRoom.objects.filter(userInfo=user)
        serializer = SimplifiedMatchingSerializer(queryset, many=True)
        return Response(serializer.data, status=200)

    def create(self, request, **kwargs):
        requestData = request.data
        userId = request.user.username
        reqCity = requestData["city"]
        reqCounty = requestData["county"]
        reqDistrict = requestData["district"]
        matchlistQuery = MatchingRoom.objects.all().values_list('city', 'county', 'district')
        matchlist = []
        for matchloc in matchlistQuery:
            matchlist.append(' '.join(matchloc))

        nearLoc = findNearbyLocation(reqCity+" "+reqCounty+" "+reqDistrict,
                                     matchlist)

        if nearLoc is not None:
            loclist = nearLoc.split(" ")
            reqCity = loclist[0]
            reqCounty = loclist[1]
            reqDistrict = loclist[2]

        try:
            userInfo = UserProfile.objects.get(userid=userId)
        except ObjectDoesNotExist:
            return Response({"error": "can't find user information from given credentials."}, status=400)
        roomInfo = reqCity + reqCounty + reqDistrict + requestData["game"]
        roomInfo = hashlib.sha1(roomInfo.encode('utf-8')).hexdigest()

        try:
            matchInExist = MatchingRoom.objects.get(matchId=roomInfo)
            matchInExist.userInfo.add(userInfo)
            test = MatchingRoom.objects.filter(matchId=roomInfo)
            serializer = MatchingSerializer(test, many=True)
            return Response({"matchId": roomInfo}, status=200)
        except ObjectDoesNotExist:
            requestData = {"userInfo": userInfo.__dict__,
                        "city": reqCity,
                        "county": reqCounty,
                        "district": reqDistrict,
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
    def list(self, request, **kwargs):
        voteid = request.GET.get("voteId", None)

        try:
            vote = Vote.objects.get(voteId=voteid)
            serializer =VoteSerializer(vote)
            return Response(serializer.data, status=200)
        except ObjectDoesNotExist:
            return Response({"error": "can't find vote from given voteId."}, status=400)

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
            userinfodata = {"vote": savedVote.voteId,
                            "userId": userdata.userid,
                            "profilePic": str(userdata.profilePic),
                            "like": 0,
                            "liker": []}
            voteserializer = VoteProfileSerializer(data = userinfodata)
            if(voteserializer.is_valid()):
                voteserializer.save()
            else:
                return Response(voteserializer.errors)
            match = MatchingRoom.objects.get(matchId=data["matchId"])
            match.voteInfo.add(savedVote)
            return Response(serializer.data, status=200)
        return Response(serializer.errors, status=400)

    def update(self, request, **kwargs):
        data = request.data
        userdata = UserProfile.objects.get(userid=request.user.username)

        userinfodata = {"vote": data["voteId"],
                        "userId": userdata.userid,
                        "profilePic": str(userdata.profilePic),
                        "like": 0,
                        "liker": []}
        voteserializer = VoteProfileSerializer(data = userinfodata)

        if(voteserializer.is_valid()):
            voteserializer.save()
        return Response(voteserializer.errors)

class LikeViewSet(viewsets.ViewSet):
    def create(self, request, **kwargs):
        data = request.data

        targetId = data["targetId"]
        voteId = data["voteId"]

        vote = Vote.objects.get(voteId=voteId)
        votetarget = vote.userList.get(userId=targetId)

        if request.user.username == targetId:
            return Response({"error": "You can't add like yourself."}, status=400)
        if request.user.username in votetarget.liker:
            return Response({"error": "You aleady liked this user."}, status=400)

        votetarget.like += 1
        votetarget.liker.append(request.user.username)
        votetarget.save()
        return Response("", status=200)
