def process(inp: [str]):
    changes = [int(x.strip()) for x in inp]

    current = 0
    visited = set([current])
    
    while True:
        for change in changes:
            current += change
            if current in visited:
                return current
            visited.add(current)


def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()