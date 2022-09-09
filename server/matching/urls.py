from django.urls import path
from .views import MatchingRoomViewSet, MatchingRoomVoteViewSet, MatchingDetailedInfo, LikeViewSet

matchingView = MatchingRoomViewSet.as_view({
    "get": "list",
    "post": "create",
})

voteView = MatchingRoomVoteViewSet.as_view({
    "post": "create",
    "put": "update",
})

matchInfoView = MatchingDetailedInfo.as_view({
    "get": "list",
})

likeView = LikeViewSet.as_view({
    "post": "create",
})

urlpatterns = [
    path('matching', matchingView),
    path('matching/vote', voteView),
    path('matching/info', matchInfoView),
    path('matching/vote/like', likeView)
]
