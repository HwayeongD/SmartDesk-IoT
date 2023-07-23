from django.db import models
from django.conf import settings
# Create your models here.


class Table(models.Model):
    name = models.CharField(max_length=10)