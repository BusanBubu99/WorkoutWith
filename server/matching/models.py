from django.db import models


# Create your models here.
class MatchingRoom(models.Model):
    userLocation = models.JSONField()
    game = models.CharField(max_length=20)
    matchId = models.CharField(max_length=100)
    # userInfo = models.ForeignKey("userprofile.UserProfile", related_name="users", on_delete=models.DO_NOTHING, null=True, db_column="userlist")
    userInfo = models.ManyToManyField("userprofile.UserProfile")

