from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
#yo dawg I heard you like apis
    url(r'^api/', 'api.api.api'),
    url(r'^admin/', include(admin.site.urls)),
)
