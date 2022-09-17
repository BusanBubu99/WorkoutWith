from django.db import models


class UserProfile(models.Model):
    """
    Models for user profile.
    """
    userid = models.CharField(max_length=100, unique=True)
    name = models.CharField(max_length=100)
    profilePic = models.ImageField(
        upload_to="profilepic/",
        null=True,
        default='media/default_profileimage.png')
    tags = models.CharField(max_length=100)
    city = models.CharField(max_length=100)
    county = models.CharField(max_length=100)
    district = models.CharField(max_length=100)
