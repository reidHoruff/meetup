# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0002_auto_20150330_0157'),
    ]

    operations = [
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('body', models.CharField(max_length=1000)),
                ('time', models.TimeField(auto_now_add=True)),
                ('receiver', models.ForeignKey(related_name=b'messages_received', to='api.Usr')),
                ('sender', models.ForeignKey(related_name=b'messages_sent', to='api.Usr')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
