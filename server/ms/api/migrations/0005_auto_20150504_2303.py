# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0004_auto_20150504_2301'),
    ]

    operations = [
        migrations.AlterField(
            model_name='message',
            name='time',
            field=models.DateTimeField(auto_now_add=True),
        ),
    ]
