# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0003_message'),
    ]

    operations = [
        migrations.AlterField(
            model_name='message',
            name='time',
            field=models.DateField(auto_now_add=True),
        ),
    ]
