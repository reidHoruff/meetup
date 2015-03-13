import api

def assert_equal(a, b):
  assert cmp(a, b) == b

def assert_failure(args):
  res = api.a_create_user(args)
  assert 'success' in res and res['success'] == False

def assert_error(args, err):
  res = api.a_create_user(args)
  assert 'success' in res and res['success'] == False and 'err' in res and res['err'] == err

def assert_success(args):
  res = api.a_create_user(args)
  assert 'success' in res and res['success'] == True

# success
args = {
  'username': 'reid',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foobar',
  'password2': 'foobar',
  'sex': 'm',
  'age': '12'
}
assert_success(args)

# no data in request, username is empty
args = {}
assert_error(args, 1)

# invalid username
args = {
  'username': 're id',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foobar',
  'password2': 'foobar',
  'sex': 'm',
  'age': '12'
}
assert_error(args, 2)

# passwords dont match
args = {
  'username': 'reid',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foobar',
  'password2': 'foobars',
  'sex': 'm',
  'age': '12'
}
assert_error(args, 4)

# password too short
args = {
  'username': 'reid',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foo',
  'password2': 'foo',
  'sex': 'm',
  'age': '12'
}
assert_error(args, 5)


# invalid sex
args = {
  'username': 'reid',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foobar',
  'password2': 'foobar',
  'sex': 'q',
  'age': '12'
}
assert_error(args, 6)

# invalid age
args = {
  'username': 'reid',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foobar',
  'password2': 'foobar',
  'sex': 'm',
  'age': 'xxx'
}
assert_error(args, 7)

# age too young
args = {
  'username': 'reid',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foobar',
  'password2': 'foobar',
  'sex': 'm',
  'age': '5'
}
assert_error(args, 8)

# age too old
args = {
  'username': 'reid',
  'firstname': 'reid',
  'lastname': 'horuff',
  'password': 'foobar',
  'password2': 'foobar',
  'sex': 'm',
  'age': '120'
}
assert_error(args, 9)
