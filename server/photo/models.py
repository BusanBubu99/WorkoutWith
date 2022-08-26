from distutils.command.upload import upload
from statistics import mode
from django.db import models
from django.contrib.auth.models import User
from django.urls import reverse

# Create your models here.
class Photo(models.Model):
    author = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user')
    text = models.TextField(blank=True)
    image = models.ImageField(upload_to = 'timeline_photo/%Y/%m/%d')
    created = models.DateTimeField(auto_now_add = True)
    updated = models.DateTimeField(auto_now=True)

    def __str__(self):
        return "text : " + self.text

    class Meta:
        ordering = ['-created']

    def get_absolute_rul(self):
        return reverse('photo:detail', args=[self.id])