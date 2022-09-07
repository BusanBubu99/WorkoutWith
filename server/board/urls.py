from xml.etree.ElementTree import Comment
from django.urls import path, include

from .views import PostViewSet, CommentViewSet
from rest_framework.routers import DefaultRouter

router = DefaultRouter()
router.register('community', PostViewSet, basename='post')
router.register('comment', CommentViewSet, basename='comment')

urlpatterns = [
    path('', include(router.urls))
]