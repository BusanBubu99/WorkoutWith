from wsgiref import validate
from .models import MatchingRoom, Vote, VoteUserinfo
from rest_framework import serializers
from userprofile.serializers import ProfileSerializer


class UserinMatchingSerializer(serializers.Serializer):
    profilePic = serializers.ImageField(use_url=True)
    name = serializers.CharField(max_length=100)

# serializer for simplified user profile for matching room
class SimplifiedProfileSerializer(ProfileSerializer):
    class Meta(ProfileSerializer.Meta):
        fields = ('userid', 'profilePic')

# serializer for user profile who joined vote
class VoteProfileSerializer(serializers.ModelSerializer):
    class Meta(ProfileSerializer.Meta):
        model = VoteUserinfo
        fields = ('vote', 'userId', 'profilePic', 'like', 'liker')

# serializer for vote
class VoteSerializer(serializers.ModelSerializer):
    userList = VoteProfileSerializer(many=True, read_only=True, allow_null=True)

    class Meta:
        model = Vote
        fields = ("voteTitle", "startTime", "endTime", "date", "content", "userList", "matchId", "voteId")


# serializer for matching
class MatchingSerializer(serializers.ModelSerializer):
    userInfo = SimplifiedProfileSerializer(many=True, read_only=True)
    voteInfo = VoteSerializer(many=True, read_only=True)

    class Meta:
        model = MatchingRoom
        fields = ("city", "county", "district", "userInfo", "game", "matchId", "voteInfo")
