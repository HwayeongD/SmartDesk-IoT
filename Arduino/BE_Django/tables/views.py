from django.shortcuts import render

from rest_framework.response import Response
from django.shortcuts import get_object_or_404, get_list_or_404
from rest_framework.decorators import api_view, permission_classes
from rest_framework import status

from .models import Table

from django.http import JsonResponse
from .serializers import TableListSerializer
# Create your views here.

@api_view(['GET', 'POST'])
def table_list(req):
    if req.method == "GET":
        tables = get_list_or_404(Table)
        serializer = TableListSerializer(tables, many=True)
        return Response(serializer.data)
    elif req.method == "POST":
        serializer = TableListSerializer(data = req.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

@api_view(['GET', 'DELETE', 'PUT'])
def table_detail(req, table_pk):
    table = get_object_or_404(Table, pk=table_pk)

    if req.method == "GET":
        serializer = TableListSerializer(table)
        return Response(serializer.data)
    elif req.method == "DELETE":
        table.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    elif req.method == "PUT":
        serializer = TableListSerializer(table, data = req.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
