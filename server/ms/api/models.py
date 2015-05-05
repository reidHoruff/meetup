from django.db import models
import datetime

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

  def get_sex(self):
    return {
        True: 'm',
        False: 'f'
    }[self.is_male]

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

  def dump_basic(self):
    return {
        'username': self.username,
        'id': str(self.id),
        'firstname': self.firstname,
        'lastname': self.lastname,
        'sex': self.get_sex(),
        'age': str(self.age),
    }

  def dump(self):
    basic = self.dump_basic()
    basic['phoneid'] = self.phoneid
    basic['interests'] = self.dump_interests()
    basic['matches'] = self.find_friends()
    return basic

  def get_message_thread(self, other):
    messages = list(Message.objects.filter(sender=self, receiver=other).all()) + list(Message.objects.filter(sender=other, receiver=self).all())
    thread = list()
    for m in messages:
      thread.append((
        m.from_me(self), 
        m.body, 
        int(m.id),
        str(m.time), 
        ))
    return thread

  def dump_interests(self):
    i = {}
    for interest in self.interests.order_by('name'):
      i[str(interest.id)] = interest.name
    return i

  def find_friends(self):
    int_ids = set()
    for i in self.interests.all():
      int_ids.add(i.id)

    matches = []
    for u in Usr.objects.all():
      if u.id == self.id:
        continue
      s = u.score(int_ids)
      if s:
        matches.append({
          'score': int(s),
          'user': u.dump_basic(),
          })

    reid = Usr.objects.get(username='reid')
    garrison = Usr.objects.get(username='garrison')
    brian = Usr.objects.get(username='brian')

    print 'hello'
    if self.username != 'reid':
      matches.append({
        'score': 100,
        'user': reid.dump_basic(),
        })

    if self.username != 'brian':
      matches.append({
        'score': 100,
        'user': brian.dump_basic(),
        })

    if self.username != 'garrison':
      matches.append({
        'score': 100,
        'user': garrison.dump_basic(),
        })

    return sorted(matches, key=lambda t: t['score'], reverse=True)[:8]

  def score(self, int_id_set):
    score = 0
    for i in self.interests.all():
      if i.id in int_id_set:
        score += 1
    return score

  def send_message(self, body, receiver):
    Message.objects.create(sender=self, receiver=receiver, body=body)

  def __unicode__(self):
    return "%s (%s)" % (self.username, self.id)

class Interest(models.Model):
  name = models.CharField(max_length=20, unique=True, blank=False)

  @staticmethod
  def from_ids(ids):
    return Interest.objects.filter(id__in=ids)

  def __unicode__(self):
    return "%s (%s)" % (self.name, self.id)

class Message(models.Model):
  sender = models.ForeignKey('Usr', related_name='messages_sent')
  receiver = models.ForeignKey('Usr', related_name='messages_received')
  body = models.CharField(max_length=1000, blank=False)
  time = models.DateTimeField(auto_now_add=True)

  def from_me(self, me):
    return  bool(self.sender.id == me.id)

  def __unicode__(self):
    return "%s -> %s (%s)" % (self.sender.username, self.receiver.username, self.body)
