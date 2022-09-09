from django.db import models
import uuid


# matching room model
class MatchingRoom(models.Model):
    city = models.CharField(max_length=20)
    county = models.CharField(max_length=20)
    district = models.CharField(max_length=20)
    game = models.CharField(max_length=20)
    matchId = models.CharField(max_length=100)
    userInfo = models.ManyToManyField("userprofile.UserProfile")
    voteInfo = models.ManyToManyField("Vote")

# user info list who joined vote
class VoteUserinfo(models.Model):
    vote = models.ForeignKey("Vote", on_delete=models.CASCADE, related_name="userList", db_column="userList")
    userId = models.CharField(max_length=20)
    profilePic = models.CharField(max_length=100)
    like = models.IntegerField()
    liker = models.JSONField()

# vote model
class Vote(models.Model):
    voteId = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    voteTitle = models.CharField(max_length=30)
    startTime = models.TimeField()
    endTime = models.TimeField()
    date = models.DateField()
    content = models.CharField(max_length=500)
    matchId = models.CharField(max_length=100, default="temp")
