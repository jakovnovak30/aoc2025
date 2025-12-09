#!/bin/python3

from sys import argv
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

if __name__ == '__main__':
    input = None
    if len(argv) == 2 and argv[1] == 'example':
        input = parse_input("example.txt")
    elif len(argv) == 1:
        input = parse_input("input.txt")
    else:
        raise Exception("invalid args")

    print(part1(input))
