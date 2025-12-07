#!/bin/python3
from functools import cache

def parse_input(filename):
    out = []
    with open(filename) as file:
        for line in file:
            out.append(line.strip())
    return out

# globals
splited_coords = set()
input = []

@cache
def count_beams(x, y):
    global splited_coords
    global input

    X = len(input[0])
    Y = len(input)

    if x < 0 or x >= X:
        return
    elif y < 0 or y >= Y:
        return

    if input[y][x] == '.' or input[y][x] == 'S':
        count_beams(x, y+1)
        return
    elif input[y][x] == '^':
        splited_coords.add((x, y))

        count_beams(x-1, y)
        count_beams(x+1, y)
        return
    

if __name__ == '__main__':
    input = parse_input("input.txt")
    start_x = 0
    start_y = 0
    for i, e in enumerate(input[0]):
        if e == "S":
            start_x = i

    count_beams(start_x, start_y)
    print(len(splited_coords))
