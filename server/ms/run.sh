ifconfig eth0 | grep inet | awk '{ print $2 }'
python manage.py runserver 0.0.0.0:8000
