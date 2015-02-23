# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Interest',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(unique=True, max_length=20)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Usr',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('username', models.CharField(unique=True, max_length=20)),
                ('firstname', models.CharField(max_length=20, blank=True)),
                ('lastname', models.CharField(max_length=20, blank=True)),
                ('password', models.CharField(max_length=100, blank=True)),
                ('phoneid', models.CharField(max_length=100, blank=True)),
                ('interests', models.ManyToManyField(to='api.Interest')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
