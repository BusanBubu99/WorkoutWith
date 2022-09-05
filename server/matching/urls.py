from django.urls import path
from .views import MatchingRoomViewSet, MatchingRoomVoteViewSet

matchingView = MatchingRoomViewSet.as_view({
    "get": "list",
    "post": "create",
})

voteView = MatchingRoomVoteViewSet.as_view({
    "post": "create",
})

urlpatterns = [
    path('matching', matchingView),
    path('matching/vote', voteView)
]