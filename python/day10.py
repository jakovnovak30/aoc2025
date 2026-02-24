#!/bin/python3

from sys import argv
import z3

def parse_input(input):
    for l in input:
        groups = l.split(' ')

        indicators = groups[0]
        wiring = groups[1:-1]
        joltage = groups[-1]

        indicators = list(map(lambda x: x == '#', filter(lambda x: x != ']' and x != '[', indicators)))
        wiring = list(map(
                      lambda x: set(map(lambda x: int(x), filter(lambda x: x not in ['(', ')', ','], x))), wiring))
        joltage = set(map(lambda x: int(x), filter(lambda x: x not in ['{', '}', ','], joltage)))

        yield indicators, wiring, joltage
        '''
        print(f'indicators: {indicators}')
        print(f'wiring: {wiring}')
        print(f'joltage: {joltage}')
        if l != input[-1]: print()
        '''

def calculate_min(indicators, wiring) -> int:
    solver = z3.Optimize()
    button_presses = z3.IntVector('b_presses', len(wiring))
   
    # save which wires activate which indicators, index is indicator, element is wire
    wire_indicator = [set()] * len(indicators)

    for i, wire in enumerate(wiring):
        solver.add(button_presses[i] >= 0)
        for indicator in wire:
            wire_indicator[indicator] = wire_indicator[indicator] | set({i})

    for indicator_index, wires in enumerate(wire_indicator):
        sumation = z3.Sum([button_presses[w] for w in wires])

        if indicators[indicator_index]:
            # needs to be pressed odd number of times
            solver.add(sumation % 2 == 1)
        else:
            # needs to be pressed even number of times
            solver.add(sumation % 2 == 0)

    total_presses = z3.Int('total_presses')
    solver.add(total_presses == z3.Sum(button_presses))
    solver.minimize(total_presses)
    solver.check()
    solver.model()

    return solver.model()[total_presses].as_long()

def part1(input) -> int:
    minimum = 0
    for indicators, wiring, _ in parse_input(input):
        minimum += calculate_min(indicators, wiring)

    return minimum

if __name__ == '__main__':
    input = []
    infile = './example.txt'
    if len(argv) == 2 and argv[1] == 'REAL': infile = './input.txt'
    with open(infile, 'r') as f:
        for line in f:
            input.append(line.strip())

    print(part1(input))
