from unittest import TestCase
from django.shortcuts import render
from django.core.exceptions import ObjectDoesNotExist
from rest_framework import viewsets
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .serializers import MatchingSerializer
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
        location = requestData["userLocation"]
        userId = request.user.username
        try:
            userInfo = UserProfile.objects.get(userid=userId)
        except ObjectDoesNotExist:
            return Response({"error": "can't find user information from given credentials."}, status=400)
        roomInfo = location["city"] + location["county"] + location["district"] + requestData["game"]
        roomInfo = hashlib.sha1(roomInfo.encode('utf-8')).hexdigest()

        try:
            matchInExist = MatchingRoom.objects.get(matchId=roomInfo)
            matchInExist.userInfo.add(userInfo)
            test = MatchingRoom.objects.filter(matchId=roomInfo)
            serializer = MatchingSerializer(test, many=True)
            return Response({"matchId": roomInfo}, status=200)
        except ObjectDoesNotExist:
            requestData = {"userInfo": userInfo.__dict__,
                        "userLocation": location,
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