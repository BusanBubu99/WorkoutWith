from userprofile.models import UserProfile
from rest_framework import serializers

class LocationSerializer(serializers.Serializer):
    city = serializers.CharField(max_length=10)
    county = serializers.CharField(max_length=10)
    district = serializers.CharField(max_length=10)
    # fields = ('city', 'county', 'district')

class ProfileSerializer(serializers.HyperlinkedModelSerializer):
    profilePic = serializers.ImageField(use_url=True)
    userLocation = LocationSerializer()

    class Meta:
        model = UserProfile
        fields = ('userid', 'name', 'profilePic', 'tags', 'userLocation')