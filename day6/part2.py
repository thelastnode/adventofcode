import enum
from collections import defaultdict
from itertools import product
from typing import List, Tuple, NamedTuple, Dict


MAX_VAL = 10_000

def distance(c1, c2) -> int:
    (x1, y1), (x2, y2) = c1, c2
    return abs(x1 - x2) + abs(y1 - y2)

def parse(line: str) -> (int, int):
    return tuple(int(word) for word in line.split(', '))

def process(inp: [str]):
    origins = { str(i): parse(x.strip()) for i, x in enumerate(inp) if x.strip() }

    min_x, min_y = min(x for x, y in origins.values()), min(y for x, y in origins.values())
    max_x, max_y = max(x for x, y in origins.values()), max(y for x, y in origins.values())

    region_size = 0

    for x in range(min_x, max_x + 1):
        for y in range(min_y, max_y + 1):
            coord = (x, y)
            dist = sum(distance(origin, coord) for origin in origins.values())
            if dist < MAX_VAL:
                region_size += 1

    return region_size

def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()