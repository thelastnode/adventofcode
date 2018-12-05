from itertools import combinations

def find_common(names: [str]):
    for (name1, name2) in combinations(names, 2):
        common_letters = (1 for c1, c2 in zip(name1, name2) if c1 == c2)
        count = sum(common_letters, 0)
        if len(name1) == len(name2) and count == len(name1) - 1:
            return (name1, name2)

def process(inp: [str]):
    names = [x.strip() for x in inp]

    name1, name2 = find_common(names)
    return ''.join([c1 for (c1, c2) in zip(name1, name2) if c1 == c2])


def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()