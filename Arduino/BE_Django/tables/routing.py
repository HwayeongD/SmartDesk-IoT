from django.urls import path

from . import consumers

websocket_urlpatterns = [
    path('tables/list/', consumers.ChatConsumer.as_asgi()),
]