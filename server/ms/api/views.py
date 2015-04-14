from django.http import HttpResponse

def home(request):
  return HttpResponse("api server for meetup.")
