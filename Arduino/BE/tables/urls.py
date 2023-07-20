
from django.contrib import admin
from django.urls import path, include
from tables import views


urlpatterns = [
    path('list/', views.table_list),
    path('detail/<int:table_pk>/', views.table_detail)
]