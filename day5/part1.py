def step(pattern: str) -> str:
    for i, (x, y) in enumerate(zip(pattern, pattern[1:])):
        if x.lower() == y.lower() and x.isupper() != y.isupper():
            return pattern[:i] + pattern[i + 2:]
    return pattern

def process(inp: [str]):
    pattern = ''.join(x.strip() for x in inp)

    next_pattern = step(pattern)

    while next_pattern != pattern:
        pattern = next_pattern
        next_pattern = step(pattern)

    return len(pattern)


def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()