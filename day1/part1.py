def process(inp: [str]):
    changes = [int(x.strip()) for x in inp]

    current = 0
    for change in changes:
        current += change
    
    return current


def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()