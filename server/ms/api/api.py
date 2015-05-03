from django.shortcuts import render
from easy.decorators import *
from models import *
from random import randint
import re

"""
helpers...
"""

def fail(msg='unknown error', err=0):
  return {'success': False, 'msg': msg, 'err': err}

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

@validate
def a_login(args, user):
  return fin('login successful', user.dump())

def a_create_user(args):
  username = get(args, 'username')
  firstname = get(args, 'firstname')
  lastname = get(args, 'lastname')
  password = get(args, 'password')
  sex = get(args, 'sex')
  age = get(args, 'age')

  username_policy = re.compile("^[a-zA-Z_0-9]+$")

  if len(username) < 1:
    return fail('Username cannot be empty', 1)

  if not username_policy.match(username):
    return fail("'%s' is not a valid username" % username, 2)

  if not Usr.username_available(username):
    return fail("Username '%s' is not available" % username, 3)

  if len(password) < 4:
    return fail('Password is too short', 5)

  if sex not in ['m', 'f'] or not age.isdigit():
    return fail('invalid sex choice', 6)

  if not age.isdigit():
    fail('age must be integer', 7)

  age = int(age)

  if age < 13:
    return fail('you must be at least 13 years old to use this service', 8)

  if age > 120:
    return fail('I don\'t believe that you are over 120 years old', 9)

  user = Usr.objects.create(
    firstname=firstname, 
    lastname=lastname, 
    username=username, 
    password=password,
    is_male=(sex=='m'),
    age=age
  )

  return fin('user created', user.dump())

@validate
def a_set_interests(args, user):
  strids = get(args, 'ids')
  ids = []
  if strids:
    ids = map(int, strids.split(","))
  interests = Interest.from_ids(ids)
  user.set_interests(interests)
  return fin("interests updated")

def a_list_all_intersts(args):
  interests = {i.id: i.name for i in Interest.objects.order_by('name')}
  return fin(data=interests)

@validate
def a_list_my_intersts(args, user):
  interests = {i.id: i.name for i in user.interests.all()}
  return fin(data=interests)

@validate
def a_get_profile(args, user):
  id = get(args, 'uid')
  if id:
    return fin(data=Usr.objects.get(id=id).dump())
  else:
    return fin(data=user.dump())

@validate
def a_get_matches(args, user):
  pass

def create_user_from_file(fname, is_male):
  fnames = open(fname)
  for n in fnames:
    ns = n.strip().split(' ')
    print ns[0], ns[1]
    u = Usr.objects.create(firstname=ns[0], lastname=ns[1], is_male=is_male, age=randint(20, 30), username=ns[0]+ns[1], password="pass")
    ints = Interest.objects.order_by('?')[:randint(15, 40)]
    u.interests.add(*ints)

def a_readin(args):
  Interest.objects.all().delete()
  Usr.objects.all().delete()

  fint = open('interests')
  interests = list()
  for i in fint:
    i = i.strip()
    interests.append(i)
    Interest.objects.create(name=i)

  create_user_from_file('female_names', False)
  create_user_from_file('male_names', True)
  return fin("readin complete")

@validate
def a_send_message(args, user):
  body = get(args, 'body')
  rid = get(args, 'rid')
  try:
    receiver = Usr.objects.get(id=rid)
    user.send_message(body, receiver)
    return fin('message sent')
  except:
    return fail('Message failed to send')

@validate
def a_get_thread(args, user):
  oid = get(args, 'oid')
  other = Usr.objects.get(id=oid)
  return fin(data=user.get_message_thread(other))

def a_ping(args):
  return fin('pong')

actions = {
    'create_user': a_create_user,
    'login': a_login,
    'get_profile': a_get_profile,
    'set_interests': a_set_interests,
    'list_all_interests': a_list_all_intersts,
    'list_my_interests': a_list_my_intersts,
    'get_matches': a_get_matches,
    'ping': a_ping,
    'readin': a_readin,
    'send_message': a_send_message,
    'get_thread': a_get_thread,
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
