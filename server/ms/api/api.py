from django.shortcuts import render
from easy.decorators import *
from models import *


"""
helpers...
"""

def fail(msg='unknown error'):
  return {'success': False, 'msg': msg}

def fin(msg='operation completed', data={}):
  return {'success': True, 'msg': msg, 'data': data}

def a_login(args):
  username = args['username']
  password = args['password']

  if Usr.validate(username=username, password=password):
    return fin('login success')
  else:
    return fail('login failure')

"""
Wrap around any action function where user auth is required,
function will authenticate the request and provide the wrapped
function the the Usr instance
"""
def validate(action):
  def wrapper(args):
    username = args['username']
    password = args['password']
    user = Usr.get_user(username=username, password=password)
    if user:
      return action(args, user)
    else:
      return fail("unable to authenticate user")
  return wrapper


"""
handlers...
"""

def a_create_user(args):
  username = args['username']
  firstname = args['firstname']
  lastname = args['lastname']
  password = args['password']

  if not Usr.username_available(username):
    return fail('username is not available')

  Usr.objects.create(
      firstname=firstname, 
      lastname=lastname, 
      username=username, 
      password=password
    )

  return fin('user created')

@validate
def a_set_interests(args, user):
  ids = map(int, args['ids'].split(","))
  interests = Interest.from_ids(ids)
  user.set_interests(interests)
  return fin("interests updated")


def a_list_all_intersts(args):
  interests = {i.id: i.name for i in Interest.objects.all()}
  return fin(data=interests)

actions = {
    'create': a_create_user,
    'login': a_login,
    'set_interests': a_set_interests,
    'list_all_interests': a_list_all_intersts,
    }

@json_response
def api(request):
  if 'a' not in request.REQUEST:
    return fail('action not specified')
  elif request.REQUEST['a'] not in actions:
    return fail('action not supported')
  else:
    return actions[request.REQUEST['a']](dict(request.REQUEST))


