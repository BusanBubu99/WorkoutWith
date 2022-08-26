from django.db import models

# Create your models here.
class UserProfile(models.Model):
    userid = models.CharField(max_length=15, unique=True)
    name = models.CharField(max_length=10)
    profilePic = models.ImageField()
    tags = models.CharField(max_length=10)
