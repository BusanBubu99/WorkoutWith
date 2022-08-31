from django.shortcuts import render
from django.core.exceptions import ObjectDoesNotExist
from rest_framework import viewsets
from rest_framework.response import Response

from .serializers import ProfileSerializer
from .models import UserProfile

import json

# Create your views here.
class ProfileViewSet(viewsets.ViewSet):
    def list(self, request, **kwargs):
        targetId = request.GET.get("targetId", None)
        if targetId is None:
            return Response("Query \'targetId\' is required to get user profile.", status=400)
        try:
            profileObject = UserProfile.objects.get(userid=targetId)
            serializer = ProfileSerializer(profileObject)
            return Response(serializer.data, status=200)
        except ObjectDoesNotExist:
            return Response(f'Can\'t find profile information with user id {targetId}.', status=400)

    def create(self, request, **kwargs):
        # TODO: need to pass userid from request by authorizations

        requestData = {"userid": request.POST.get("userid"),
                       "name": request.POST.get("name"),
                       "profilePic": request.FILES.get("profilePic"),
                       "tags": request.POST.get("tags"), 
                       "userLocation": json.loads(request.POST.get("userLocation", "{}"))}
        
        serializer = ProfileSerializer(data=requestData)
        if serializer.is_valid():
            print(serializer.validated_data)
            serializer.save()
            return Response(serializer.data, status=200)
        return Response(serializer.errors, status=400)
