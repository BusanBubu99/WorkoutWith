from userprofile.models import UserProfile
from rest_framework import serializers


class ProfileSerializer(serializers.HyperlinkedModelSerializer):
    profilePic = serializers.ImageField(use_url=True)

    class Meta:
        model = UserProfile
        fields = ('userid', 'name', 'profilePic',
                  'tags', 'city', 'county', 'district')
