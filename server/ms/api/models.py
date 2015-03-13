from django.db import models

class Usr(models.Model):
  username = models.CharField(max_length=20, unique=True, blank=False)
  firstname = models.CharField(max_length=20,blank=True)
  lastname = models.CharField(max_length=20, blank=True)
  password = models.CharField(max_length=100, blank=True)
  phoneid = models.CharField(max_length=100, blank=True)
  interests = models.ManyToManyField('Interest')
  age = models.IntegerField()
  is_male = models.BooleanField(default=True)

  @staticmethod
  def validate(username, password):
    return Usr.objects.filter(username=username).filter(password=password).count() == 1

  @staticmethod
  def get_user(username, password):
    try:
      return Usr.objects.get(username=username, password=password)
    except:
      return None

  @staticmethod
  def username_available(username):
    return Usr.objects.filter(username=username).count() == 0

  def set_interests(self, interest_objects):
    self.interests.clear()
    for interests in interest_objects:
      self.interests.add(interests)

  def dump(self):
    return {
        'username': self.username,
        'firstname': self.firstname,
        'lastname': self.lastname,
        'phoneid': self.phoneid,
        }


  def __unicode__(self):
    return self.username

class Interest(models.Model):
  name = models.CharField(max_length=20, unique=True, blank=False)

  @staticmethod
  def from_ids(ids):
    return Interest.objects.filter(id__in=ids)

  def __unicode__(self):
    return "%s (%s)" % (self.name, self.id)



