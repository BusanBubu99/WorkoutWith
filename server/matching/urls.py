from django.urls import path
from .views import MatchingRoomViewSet

matchingView = MatchingRoomViewSet.as_view({
    "get": "list",
    "post": "create",
})

urlpatterns = [
    path('matching', matchingView)
]