from django.conf.urls import include
from django.urls import path

from .views import ProfileViewSet

profileView = ProfileViewSet.as_view({
    "get": "list",
    "post": "create",
})

urlpatterns = [
    path('profile', profileView)
]