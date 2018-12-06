import enum
from collections import defaultdict
from itertools import product
from typing import List, Tuple, NamedTuple, Dict


class GridState(NamedTuple):
    origin: str
    distance: int

class QueueItem(NamedTuple):
    origin: str
    distance: int
    coord: Tuple[int]


def print_grid(grid, x_range, y_range):
    for y in y_range:
        print('|', end='')
        for x in x_range:
            print(grid.get((x, y), GridState(origin=' ', distance=0)).origin, end='')
        print('|')

def grid_values(grid):
    counts_by_origin = defaultdict(int)
    for grid_item in grid.values():
        counts_by_origin[grid_item.origin] += 1
    
    return sorted(counts_by_origin.values())

def neighbors(coord: Tuple[int]) -> List[Tuple[int]]:
    x, y = coord
    return [(x + dx, y + dy) for dx in [-1, 0, 1] for dy in [-1, 0, 1] if abs(dx) + abs(dy) == 1]

def parse(line: str) -> (int, int):
    return tuple(int(word) for word in line.split(', '))

def process(inp: [str]):
    origins = { str(i): parse(x.strip()) for i, x in enumerate(inp) if x.strip() }

    grid = {}
    queue = []

    for origin, coord in origins.items():
        grid[coord] = GridState(origin=origin, distance=0)
        for neighbor in neighbors(coord):
            queue.append(QueueItem(origin=origin, distance=1, coord=neighbor))
    
    while len(grid) < 1_000_000:
        if len(grid) % 10_000 == 0:
            last_values = grid_values(grid)
            print(f'Grid coverage: {len(grid)}')
        item = queue.pop(0)

        if item.coord not in grid:
            # first one here!
            grid[item.coord] = GridState(origin=item.origin, distance=item.distance)
            for neighbor in neighbors(item.coord):
                queue.append(QueueItem(origin=item.origin, distance=item.distance + 1, coord=neighbor))
        elif item.distance == grid[item.coord].distance and item.origin != grid[item.coord].origin and grid[item.coord].origin != '.':
            # second here, but same distance away
            grid[item.coord] = GridState(origin='.', distance=grid[item.coord].distance)
        else:
            # second here, farther away
            pass

    print(last_values)

    return grid_values(grid)

def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()