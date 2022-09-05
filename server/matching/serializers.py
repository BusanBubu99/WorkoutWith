from wsgiref import validate
from .models import MatchingRoom, Vote
from rest_framework import serializers
from userprofile.serializers import ProfileSerializer


class UserinMatchingSerializer(serializers.Serializer):
    profilePic = serializers.ImageField(use_url=True)
    name = serializers.CharField(max_length=100)

class SimplifiedProfileSerializer(ProfileSerializer):
    class Meta(ProfileSerializer.Meta):
        fields = ('userid', 'profilePic')

class VoteProfileSerializer(ProfileSerializer):
    like = serializers.IntegerField(min_value=0)
    class Meta(ProfileSerializer.Meta):
        fields = ('userid', 'profilePic', 'like')



class VoteTimeSerializer(serializers.Serializer):
    startTime = serializers.TimeField()
    endTime = serializers.TimeField()
    date = serializers.DateField()

class VoteSerializer(serializers.ModelSerializer):
    userList = VoteProfileSerializer(many=True, read_only=True)

    class Meta:
        model = Vote
        fields = ("voteTitle", "startTime", "endTime", "date", "content", "userList")

class MatchingSerializer(serializers.ModelSerializer):
    userInfo = SimplifiedProfileSerializer(many=True, read_only=True)
    voteInfo = VoteSerializer(many=True, read_only=True)

    class Meta:
        model = MatchingRoom
        fields = ("city", "county", "district", "userInfo", "game", "matchId", "voteInfo")
