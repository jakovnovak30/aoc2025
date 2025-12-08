#!/bin/python3

from sys import argv, maxsize
from functools import cache

# globals
connections = []
visited = set()

def parse_input(filename):
    out = []

    with open(filename, 'r') as file:
        for line in file:
            out.append(tuple(map(int, line.strip().split(','))))
    return out
    
@cache
def dist(point1, point2):
    x1, y1, z1 = point1
    x2, y2, z2 = point2

    d = (x1 - x2)**2 + (y1 - y2)**2 + (z1 - z2)**2
    return d

def not_connected(i, j):
    global connections

    for connection in connections:
        if i in connection and j in connection:
            return False

    return True

def find_shortest_connection(points):
    min = maxsize
    min_points = None
    for i in range(len(points)):
        for j in range(i+1, len(points)):
            e1 = points[i]
            e2 = points[j]

            if (i, j) in visited:
                continue

            if dist(e1, e2) < min:
                min = dist(e1, e2)
                min_points = (i, j)

    assert min_points is not None

    visited.add(min_points)
    '''
    i, j = min_points
    print(f"DEBUG: min_points are {points[i]} and {points[j]}")
    '''
    return min_points

def find_shortest_connection2(points):
    min = maxsize
    min_points = None
    for i in range(len(points)):
        for j in range(i+1, len(points)):
            e1 = points[i]
            e2 = points[j]

            if dist(e1, e2) < min and not_connected(i, j):
                min = dist(e1, e2)
                min_points = (i, j)

    assert min_points is not None
    i, j = min_points
    '''
    i, j = min_points
    print(f"DEBUG: min_points are {points[i]} and {points[j]}")
    '''
    return min_points

if __name__ == '__main__':
    input = None
    if len(argv) == 1:
        input = parse_input("input.txt")
    elif len(argv) == 2 and argv[1] == 'example':
        input = parse_input("example.txt")
    else:
        raise Exception("invalid args")

    for i in range(len(input)):
        connections.append(set([i]))

    # part 1
    '''
    for _ in range(1000):
        min_points = find_shortest_connection(input)
        if min_points is None: continue
        i, j = min_points
        set1 = None
        set2 = None
        for connection in connections:
            if i in connection:
                set1 = connection
            if j in connection:
                set2 = connection
        assert set1 is not None and set2 is not None

        if set1 != set2:
            connections.remove(set1)
            connections.remove(set2)
            connections.append(set1 | set2)

    lista = list(map(lambda x: len(x), connections))
    lista = sorted(lista, reverse=True)[:3]
    print(lista[0]*lista[1]*lista[2])
    '''

    while len(connections) > 1:
        min_points = find_shortest_connection2(input)
        i, j = min_points
        set1 = None
        set2 = None
        for connection in connections:
            if i in connection:
                set1 = connection
            if j in connection:
                set2 = connection
        assert set1 is not None and set2 is not None

        connections.remove(set1)
        connections.remove(set2)
        connections.append(set1 | set2)

        if len(connections) == 1:
            e1 = input[i]
            e2 = input[j]
            print(e1[0] * e2[0])
