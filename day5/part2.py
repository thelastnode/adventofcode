def step(pattern: str) -> str:
    for i, (x, y) in enumerate(zip(pattern, pattern[1:])):
        if x.lower() == y.lower() and x.isupper() != y.isupper():
            return pattern[:i] + pattern[i + 2:]
    return pattern

def process_pattern(pattern: str) -> int:
    next_pattern = step(pattern)

    while next_pattern != pattern:
        pattern = next_pattern
        next_pattern = step(pattern)

    return len(pattern)

def without_char(pattern: str, char: str) -> str:
    print(f'Removing char: {char}')
    return pattern.replace(char.lower(), '').replace(char.upper(), '')

def process(inp: [str]):
    pattern = ''.join(x.strip() for x in inp)
    chars = set(pattern.lower())
    options = [(char, process_pattern(without_char(pattern, char))) for char in chars]
    removed_char, pattern_len = min(options, key=lambda x: x[1])

    return removed_char, pattern_len

def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()