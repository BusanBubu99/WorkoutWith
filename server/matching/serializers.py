from .models import MatchingRoom
from rest_framework import serializers
from userprofile.serializers import ProfileSerializer, LocationSerializer


class UserinMatchingSerializer(serializers.Serializer):
    profilePic = serializers.ImageField(use_url=True)
    name = serializers.CharField(max_length=100)

class SimplifiedProfileSerializer(ProfileSerializer):
    class Meta(ProfileSerializer.Meta):
        fields = ('userid', 'profilePic')

class MatchingSerializer(serializers.ModelSerializer):
    userInfo = SimplifiedProfileSerializer(many=True, read_only=True)
    userLocation = LocationSerializer()

    class Meta:
        model = MatchingRoom
        fields = ("userInfo", "userLocation", "game", "matchId")
