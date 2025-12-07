#!/bin/python3

import sys

def parse_input(filename : str):
    out = []
    with open(filename, 'r') as file:
        for line in file:
            out.append(line[:-1])

    operands = out[:-1]
    operators = list(filter(lambda x: x!='', out[-1].split(' ')))
    return operands, operators

if __name__=='__main__':
    input = None
    if len(sys.argv) == 1:
        input = parse_input("input.txt")
    elif len(sys.argv) == 2 and sys.argv[1] == "example":
        input = parse_input("example.txt")
    else:
        raise Exception("invalid args")

    #print(input)
    operands, operators = input
    current_operator = 0
    n = len(operands[0])
    acc = 0 # total accumulator
    curr_acc = None # accumulator for current operand
    for i in range(n+1):
        curr_num = 0
        number_seen = False
        for operand in operands:
            if i == n:
                break
            if operand[i].isnumeric():
                number_seen = True
                curr_num = int(operand[i]) + curr_num * 10

        op = operators[current_operator]
        if curr_acc == None:
            if op == '+':
                curr_acc = 0
            elif op == '*':
                curr_acc = 1
            else:
                raise Exception("unknown op")

        if curr_num == 0:
            pass
        elif op == '+':
            curr_acc += curr_num
        elif op == '*':
            curr_acc *= curr_num
        else:
            raise Exception("unknown op")

        #print(f"curr_num: {curr_num}, curr_op: {operators[current_operator]}")
        if not number_seen:
            #print(f"operator done, result: {curr_acc}")
            acc += curr_acc
            curr_acc = None
            current_operator += 1
    print(acc)
