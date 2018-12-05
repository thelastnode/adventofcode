from typing import NamedTuple

class Rect(NamedTuple):
    id: str
    x: int
    y: int
    width: int
    height: int

def parse(inp: str) -> Rect:
    # example = '#4 @ 727,230: 26x26'

    id, _, pos, dim = inp.split(' ')
    pos = pos[:-1] # strip off ':'
    x, y = pos.split(',')
    width, height = dim.split('x')

    return Rect(id=id, x=int(x), y=int(y), height=int(height), width=int(width))

def process(inp: [str]):
    rects = [parse(x.strip()) for x in inp]

    left = min(rect.x for rect in rects)
    right = max(rect.x + rect.width for rect in rects)
    top = min(rect.y for rect in rects)
    bottom = max(rect.y + rect.height for rect in rects)

    grid = [[0 for y in range(bottom)] for x in range(right)]

    for rect in rects:
        for x in range(rect.x, rect.x + rect.width):
            for y in range(rect.y, rect.y + rect.height):
                grid[x][y] += 1

    return sum(1 for x in range(right) for y in range(bottom) if grid[x][y] > 1)


def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()