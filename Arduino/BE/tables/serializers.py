from rest_framework import serializers
from .models import Table

class TableListSerializer(serializers.ModelSerializer):
    class Meta:
        model = Table
        fields = '__all__'