from django.shortcuts import render
from easy.decorators import *
from models import *
import re

"""
helpers...
"""

def fail(msg='unknown error'):
  return {'success': False, 'msg': msg}

def fin(msg='operation completed', data={}):
  return {'success': True, 'msg': msg, 'data': data}

"""
is request a test?
"""
def test(args):
  if 't' not in args:
    return False
  return args['t'] != 'false'

"""
safe query param fetching
"""
def get(args, key):
  value = args.get(key)
  if value:
    return value
  return ''

"""
Wrap around any action function where user auth is required,
function will authenticate the request and provide the wrapped
function the the Usr instance
"""
def validate(action):
  def wrapper(args):
    username = get(args, 'username')
    password = get(args, 'password')
    user = Usr.get_user(username=username, password=password)
    if user:
      return action(args, user)
    else:
      return fail("unable to authenticate user")
  return wrapper

"""
handlers...
"""

def a_login(args):
  username = get(args, 'username')
  password = get(args, 'password')

  if Usr.validate(username=username, password=password):
    return fin('login success')
  else:
    return fail('login failure')

def a_create_user(args):
  username = get(args, 'username')
  firstname = get(args, 'firstname')
  lastname = get(args, 'lastname')
  password = get(args, 'password')
  password2 = get(args, 'password2')

  username_policy = re.compile("^[a-zA-Z_0-9]+$")

  if len(username) < 1:
    return fail('Username cannot be empty')

  if not username_policy.match(username):
    return fail("'%s' is not a valid username" % username)

  if not Usr.username_available(username):
    return fail("Username '%s' is not available" % username)

  if password != password2:
    return fail('Passwords do not match')

  if len(password) < 4:
    return fail('Password is too short')

  user = Usr.objects.create(
    firstname=firstname, 
    lastname=lastname, 
    username=username, 
    password=password
  )

  return fin('user created', user.dump())

@validate
def a_set_interests(args, user):
  ids = map(int, get(args, 'ids').split(","))
  interests = Interest.from_ids(ids)
  user.set_interests(interests)
  return fin("interests updated")

def a_list_all_intersts(args):
  interests = {i.id: i.name for i in Interest.objects.all()}
  return fin(data=interests)

@validate
def a_list_my_intersts(args, user):
  interests = {i.id: i.name for i in user.interests}
  return fin(data=interests)

@validate
def a_get_profile(args, user):
  return fin(data=user.dump())

actions = {
    'create_user': a_create_user,
    'login': a_login,
    'get_profile': a_get_profile,
    'set_interests': a_set_interests,
    'list_all_interests': a_list_all_intersts,
    'list_my_interests': a_list_my_intersts,
    }

"""
action request handler...
"""
@json_response
def api(request):
  if 'a' not in request.REQUEST:
    response = fail('action not specified')
  elif request.REQUEST['a'] not in actions:
    response = fail('action not supported')
  else:
    response =  actions[request.REQUEST['a']](dict(request.REQUEST))

  response['test'] = test(dict(request.REQUEST))
  return response
