# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='usr',
            name='age',
            field=models.IntegerField(default=True),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='usr',
            name='is_male',
            field=models.BooleanField(default=True),
            preserve_default=True,
        ),
    ]
