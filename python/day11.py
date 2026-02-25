#!/bin/python3

from sys import argv
from functools import cache

def parse_input(infile : str) -> dict[str, list[str]]:
    out = {}
    with open(infile, 'r') as f:
        for line in f:
            key, vals = line.split(':')
            vals = vals.strip().split(' ')
            out[key] = vals
    return out

def part1(in_map : dict[str, list[str]]) -> int:
    def rec(curr) -> int:
        if curr == 'out':
            return 1
        
        curr_outs = in_map[curr]
        suma = 0
        for out in curr_outs:
            suma += rec(out)
        return suma

    return rec('you')

def part2(in_map : dict[str, list[str]]) -> int:
    @cache
    def rec(curr, saw_fft, saw_dac) -> int:
        if curr == 'out':
            if saw_fft and saw_dac: return 1
            else: return 0
        elif curr == 'dac':
            saw_dac = True
        elif curr == 'fft':
            saw_fft = True
        
        curr_outs = in_map[curr]
        suma = 0
        for out in curr_outs:
            suma += rec(out, saw_fft, saw_dac)
        return suma

    return rec('svr', False, False)

if __name__ == '__main__':
    infile = './example.txt'
    if len(argv) == 2:
        if argv[1] == 'REAL': infile = './input.txt'
    elif len(argv) != 1:
        raise EnvironmentError('Invalid number of args!')

    in_map = parse_input(infile)

    print(f'part1: {part1(in_map)}')
    print(f'part2: {part2(in_map)}')
