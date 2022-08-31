from django.db import models


# Create your models here.
class UserProfile(models.Model):
    userid = models.CharField(max_length=15, unique=True)
    name = models.CharField(max_length=10)
    profilePic = models.ImageField(upload_to = "profilepic/", null=True, default='media/default_profileimage.png')
    tags = models.CharField(max_length=10)
    userLocation = models.JSONField()