from django.db import models


# Create your models here.
class MatchingRoom(models.Model):
    city = models.CharField(max_length=20)
    county = models.CharField(max_length=20)
    district = models.CharField(max_length=20)
    game = models.CharField(max_length=20)
    matchId = models.CharField(max_length=100)
    # userInfo = models.ForeignKey("userprofile.UserProfile", related_name="users", on_delete=models.DO_NOTHING, null=True, db_column="userlist")
    userInfo = models.ManyToManyField("userprofile.UserProfile")
    voteInfo = models.ManyToManyField("Vote")


class Vote(models.Model):
    voteTitle = models.CharField(max_length=30)
    startTime = models.TimeField()
    endTime = models.TimeField()
    date = models.DateField()
    content = models.CharField(max_length=500)
    userList = models.ManyToManyField("userprofile.UserProfile")
    