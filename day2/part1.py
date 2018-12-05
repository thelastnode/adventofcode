from collections import Counter

def process(inp: [str]):
    names = [x.strip() for x in inp]

    two_counts, three_counts = 0, 0

    for name in names:
        counts = Counter(name).values()
        if 2 in counts:
            two_counts += 1
        if 3 in counts:
            three_counts += 1

    return two_counts * three_counts


def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()