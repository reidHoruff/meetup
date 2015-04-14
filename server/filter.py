f = open('interests')
ss = set()

def p(s):
  s = s.strip()
  if len(s) > 2 and len(s) < 15:
    ss.add(s)

for l in f:
  if '[' in l:
    b = l.index('[')
    p(l[:b])
  else:
    p(l)

for q in ss:
  print q
