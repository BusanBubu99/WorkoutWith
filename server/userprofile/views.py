from django.shortcuts import render
from django.core.exceptions import ObjectDoesNotExist
from rest_framework import viewsets
from rest_framework.response import Response

from .serializers import ProfileSerializer
from .models import UserProfile

# Create your views here.
class ProfileViewSet(viewsets.ViewSet):
    def list(self, request, **kwargs):
        targetId = request.GET.get("targetId", None)
        if targetId is None:
            return Response("Query \'targetId\' is required to get user profile.", status=400)
        try:
            profileObject = UserProfile.objects.get(userid=targetId)
            serializer = ProfileSerializer(profileObject)
            if serializer.is_valid():
                return Response(serializer.data, status=200)
        except ObjectDoesNotExist:
            return Response(f'Can\'t find profile information with user id {targetId}.')