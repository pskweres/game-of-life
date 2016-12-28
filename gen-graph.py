import sys
import itertools

N = int(sys.argv[1])


def vertex_id(a, b, c):
    return a * N ** 2 + b * N + c


def vertex_loc(id):
    a = id // (N ** 2)
    b = (id - a * N ** 2) // N
    c = id % N
    return (a, b, c)


for i in range(N):
    for j in range(N):
        for k in range(N):
            x = range(i - 1, i + 2)
            y = range(j - 1, j + 2)
            z = range(k - 1, k + 2)
            neighbours = list(itertools.product(x, y, z))
            neighbours[:] = [q for q in neighbours if not (q[0] == i and q[1] == j and q[2] == k)]
            neighbours[:] = [q for q in neighbours if min(q) >= 0 and max(q) < N]
            v_id = vertex_id(i, j, k)
            print('{}\t{}'.format(v_id, 0.0), end='')
            for (a, b, c) in neighbours:
                print('\t{}\t{}'.format(vertex_id(a, b, c), 0.0), end='')
            print()
