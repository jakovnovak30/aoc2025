#!/bin/python3

from sys import argv

def parse_input(infile : str):
    areas = []
    with open(infile, 'r') as f:
        areas = f.readlines()[30:]

        elements = list(map(lambda x: x.split(':')[1].strip().split(' '), areas))
        elements = list(map(lambda x: sum(map(lambda x2: int(x2), x)), elements))
        areas = list(
                    map(lambda x: (int(x[0]), int(x[1])),
                        map(lambda x: x.split(':')[0].split('x'), areas)))

    return areas, elements

if __name__ == '__main__':
    infile = './example.txt'
    if len(argv) == 2:
        if argv[1] == 'REAL': infile = './input.txt'
    elif len(argv) != 1:
        raise RuntimeError()

    areas, elements = parse_input(infile)
    valid_ctr = 0
    for elem, area in zip(elements, areas):
        if elem * 8 < area[0] * area[1]:
            valid_ctr += 1

    print(valid_ctr)
