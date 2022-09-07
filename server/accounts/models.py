from django.contrib.auth.models import AbstractUser
from django.db import models
from django.utils.translation import gettext_lazy as _

from .managers import UserManager


class User(AbstractUser):
    username = models.CharField(default='', max_length=100,
                                null=False, blank=False, unique=True)
    email = models.EmailField(_('email address'), unique=True)

    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ['email','password']

    objects = UserManager()

    def __str__(self):
        return self.email
