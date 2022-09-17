from django.urls import path, include, re_path
from dj_rest_auth.registration.views import VerifyEmailView, RegisterView

from dj_rest_auth.views import (
    LoginView, LogoutView, PasswordChangeView,
    PasswordResetView, PasswordResetConfirmView
)
from accounts.views import ConfirmEmailView

urlpatterns = [
    path('password/reset/confirm/<uidb64>/<token>/', PasswordResetConfirmView.as_view(), name='password_reset_confirm'),
    path('password/reset', PasswordResetView.as_view(), name="rest_password_reset"),
    path('registration/', include('dj_rest_auth.registration.urls')),
    path('', include('dj_rest_auth.urls')),
    path('allauth/', include('allauth.urls')),
    path('accounts/allauth/', include('allauth.urls')),
    re_path(r'^account-confirm-email/$', VerifyEmailView.as_view(), name='account_email_verification_sent'),
    re_path(r'^account-confirm-email/(?P<key>[-:\w]+)/$', ConfirmEmailView.as_view(), name='account_confirm_email')
]
