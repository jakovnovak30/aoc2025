#!/bin/python3

from sys import argv
from itertools import combinations
import z3

def parse_input(filename):
    out = []
    with open(filename, 'r') as file:
        for line in file:
            coords = line.strip().split(',')
            coords = map(int, coords)
            out.append(tuple(coords))

    return out

def part1(input):
    s = z3.Optimize()
    xs = z3.IntVector('x', 2)
    ys = z3.IntVector('y', 2)

    for (x, y) in zip(xs, ys):
        possible_vars = []
        for e in input:
            possible_vars.append(
                z3.And(x == e[0], y == e[1]))
        s.add(z3.Or(possible_vars))

    s.add(xs[0] < xs[1], ys[0] < ys[1])

    area = z3.Int('area')
    s.add(area == (xs[1] - xs[0] + 1) * (ys[1] - ys[0] + 1))
    s.maximize(area)

    s.check()
    return s.model()[area]

def is_valid(p1, p2, polygon):
    vertical   = list(filter(lambda e: e[0][0] == e[1][0], polygon))
    horizontal = list(filter(lambda e: e[0][1] == e[1][1], polygon))

    px1, py1 = p1
    px2, py2 = p2

    min_y = min(py1, py2)
    max_y = max(py1, py2)
    min_x = min(px1, px2)
    max_x = max(px1, px2)

    '''
    (px1, py1) - - - - - - - - - (px2, py1)
         |                            |
         |                            |
         |                            |
         |                            |
         |                            |
         |                            |
    (px1, py2) - - - - - - - - - (px2, py2)
    '''

    for (x, y1), (_, y2) in vertical:
        # we have two horizontal lines:
        # px1 to px2 with y = min_y
        # and
        # px1 to px2 with y = py2

        if not (max_y <= y1 or min_y >= y2):
            if min_x < x < max_x:
                return False

    for (x1, y), (x2, _) in horizontal:
        # we have two vertical lines
        # min_y to max_y with x = min_x
        # min_y to max_y with x = max_x
        if not (max_x <= x1 or min_x >= x2):
            if min_y < y < max_y:
                return False

    return True

def part2(input):
    polygon = []

    for (e1, e2) in zip(input, input[1:] + [input[0]]):
        if e1[0] == e2[0]:
            min_y = min(e1[1], e2[1])
            max_y = max(e1[1], e2[1])
            polygon.append(((e1[0], min_y), (e1[0], max_y)))
        elif e1[1] == e2[1]:
            min_x = min(e1[0], e2[0])
            max_x = max(e1[0], e2[0])
            polygon.append(((min_x, e1[1]), (max_x, e1[1])))
        else:
            raise Exception("invalid input")

        #print(f"\t {new_greens}")

    curr_max = 0
    curr_best = None
    for (x1, y1), (x2, y2) in combinations(input, 2):
        if not is_valid((x1, y1), (x2, y2), polygon):
            continue

        area = (abs(x2 - x1) + 1) * (abs(y2 - y1) + 1)
        if area > curr_max:
            curr_max = area
            curr_best = ((x1, y1), (x2, y2))

    min_x = min(map(lambda e: e[0], input))-5
    max_x = max(map(lambda e: e[0], input))+5
    min_y = min(map(lambda e: e[1], input))-5
    max_y = max(map(lambda e: e[1], input))+5

    #print(f"[DEBUG] best: {curr_best}")
    #print(f"[DEBUG] polygon: {polygon}")
    return curr_max

if __name__ == '__main__':
    input = None
    if len(argv) == 2 and argv[1] == 'example':
        input = parse_input("example.txt")
    elif len(argv) == 1:
        input = parse_input("input.txt")
    else:
        raise Exception("invalid args")

    #print(part1(input))
    print(part2(input))
