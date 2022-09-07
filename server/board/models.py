from statistics import mode
from django.db import models
from accounts.models import User
from django.utils.timezone import now
from django.conf import settings


# Create your models here.
class Post(models.Model):
    id = models.BigAutoField(help_text="Post ID", primary_key=True, null=False, blank=False)
    title = models.CharField(max_length=200)
    picture = models.ImageField(blank=True, null=True, upload_to = "Post/images")
    contents = models.TextField()
    user = models.ForeignKey(User, null=True, blank=True, on_delete=models.CASCADE)
    date = models.DateTimeField(default=now, editable=False)

    def __str__(self):
        return self.title

class Comment(models.Model):
    id = models.AutoField(primary_key=True, null=False, blank=False)
    post = models.ForeignKey(Post, null=False, blank=False, on_delete=models.CASCADE)
    user = models.ForeignKey(User, null=False, blank=False, on_delete=models.CASCADE)
    date = models.DateTimeField(default=now, editable=False)
    comment = models.TextField()

    def __str__(self):
        return self.comment