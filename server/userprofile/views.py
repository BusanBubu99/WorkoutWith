from django.core.exceptions import ObjectDoesNotExist
from rest_framework import viewsets
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .serializers import ProfileSerializer
from .models import UserProfile

import json


class ProfileViewSet(viewsets.ViewSet):
    permission_classes_by_action = {'create': [IsAuthenticated]}

    def list(self, request, **kwargs):
        targetId = request.GET.get("targetId", None)
        isExistCheck = True if request.GET.get("check", None) == "True" \
            else False
        nullObject = {"userId": None,
                      "name": None,
                      "profilePic": None,
                      "tags": None,
                      "userLocation": None}

        if isExistCheck:
            try:
                profileObject = UserProfile.objects.get(
                    userid=request.user.username)
                return Response({"snsResult": 99}, status=200)
            except ObjectDoesNotExist:
                return Response({"snsResult": 0}, status=400)

        if targetId is None:
            return Response({"error": "Query \'targetId\' is required " +
                            "to get user profile."},
                            status=400)
        try:
            profileObject = UserProfile.objects.get(userid=targetId)
            serializer = ProfileSerializer(profileObject)
            return Response(serializer.data, status=200)
        except ObjectDoesNotExist:
            return Response(nullObject, status=400)

    def create(self, request, **kwargs):
        requestData = {"userid": request.user.username,
                       "name": request.POST.get("name"),
                       "profilePic": request.FILES.get("profilePic"),
                       "tags": request.POST.get("tags"),
                       "city": request.POST.get("city"),
                       "county": request.POST.get("county"),
                       "district": request.POST.get("district")}

        serializer = ProfileSerializer(data=requestData)
        if serializer.is_valid():
            print(serializer.validated_data)
            serializer.save()
            return Response(serializer.data, status=200)
        return Response(serializer.errors, status=400)

    def get_permissions(self):
        try:
            return [permission() for permission
                    in self.permission_classes_by_action[self.action]]
        except KeyError:
            return [permission() for permission in self.permission_classes]
