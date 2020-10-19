from z3 import *

GREEN_FILTER = True

def generate_name(*args):
  sb = ""
  for arg in args:
    sb += str(arg)
  return sb

def valid_size():

  def at_least_one_size(_k):
    lst = []
    for i in range(n):
      for j in range(i + 1, n):
        lst.append(g[i][j][_k])
    return Or(*lst)

  def at_most_one_size(_k):
    lst = []
    for i in range(n):
      for j in range(i + 1, n):
        for l in range(i + 1, n):
          for m in range(l + 1, n):
            lst.append(Or(Not(g[i][j][_k]), Not(g[l][m][_k])))
    return And(*lst)

  l = []
  for _k in range(1, k + 1):
    l.append(And(at_most_one_size(_k), at_least_one_size(_k)))
  return And(*l)

def forward_size():

  def fwd(i, j, _k, m):

    def find(i, j, m):
      b1 = (m & (1 << i)) != 0
      b2 = (m & (1 << j)) != 0

      if not b1 and b2:
        return m - (1 << j) + (1 << i)
      else:
        return None

    def c(i, j, m):
      b1 = (m & (1 << i)) != 0
      b2 = (m & (1 << j)) != 0
      if b1 and not b2:
        return m - (1 << i) + (1 << j)
      else:
        return m

    w = find(i, j, m)
    if w != None:
      assert c(i, j, w) == m
      return o[m][_k] == Or(o[m][_k - 1], o[w][_k - 1])
    else:
      if m == c(i, j, m):
        return o[m][_k] == o[m][_k - 1]
      else:
        return Not(o[m][_k])

  def fwd_update(_k, m):
    l = []
    for i in range(0, n):
      for j in range(i + 1, n):
        l.append(Implies(g[i][j][_k], fwd(i, j, _k, m)))
    return And(*l)

  lst = []
  for _k in range(1, s):
    for m in range(1 << n):
      lst.append(fwd_update(_k, m))
  return And(*lst)

def all_inputs():
  lst = []
  for m in range(1 << n):
    lst.append(o[m][0])
  return And(*lst)

def no_unsorted_outputs():

  def not_sorted():
    alll = set()
    for i in range(1 << n):
      alll.add(i)

    for i in range(n):
      nr = ((1 << n) - 1) - ((1 << i) - 1)
      alll.remove(nr)

    alll.remove(0)

    return list(alll)

  lst = []
  for m in not_sorted():
      lst.append(Not(o[m][s - 1]))
  return And(*lst)

def green_filter():
  lst = []
  lng = 1
  step = 1
  while lng < n:
    for _k in range(1, lng + 1):
      i = _k
      while i <= n - lng:
        # trebuie adaugat comparatorul (i - 1, i + lng - 1)
        lst.append(g[i - 1][i + lng - 1][step])
        step += 1
        i += 2 * lng
    lng *= 2
  return And(*lst)


sol = Solver()

## n -> numarul de circuite
## k -> numarul de comparatori
n, k = map(int, input().split())

## s -> numarul de layer-uri (presupunem ca layer 0 e inainte de comparatori)
s = k + 1

## g[i][j][k] -> avem o conexiune intre i si j la pasul k
g = [[[Bool(generate_name("g", "-", i,"-", j,"-", _k)) for _k in range(0, k + 1) ] for j in range(n)] for i in range(n)]

## o[i][j] -> avem o configuratie de tipul m la pasul j
o = [[Bool(generate_name("o", "-", i, "-", j)) for j in range(s)] for i in range(1 << n)]

print("variable count: " + str(len(g) * len(g[0]) * len(g[0][0]) + len(o) * len(o[0])))

f = And(valid_size(), forward_size(), all_inputs(), no_unsorted_outputs())

if GREEN_FILTER:
  f = And(f, green_filter())

f = simplify(f)
sol.add(f)
print("satisfiability: " + str(sol.check()))

if str(sol.check()) != "sat":
  exit()

solution = sol.model()
g_sol = [(0, 0) for _ in range(s)]
for i in solution:
  tokens = i.name().split("-")
  if tokens[0] == 'g':
    if solution[i] == True:
      g_sol[int(tokens[3])] = (tokens[1], tokens[2])

print("solution: " + str(g_sol[1:]))

# print(f)
# # Traversing statistics
for k, v in sol.statistics():
   print("%s : %s" % (k, v))