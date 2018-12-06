import enum
import re
from collections import defaultdict
from datetime import datetime, timedelta
from typing import NamedTuple, Union

class EventType(enum.Enum):
    START_SHIFT = enum.auto()
    FALL_ASLEEP = enum.auto()
    WAKE_UP = enum.auto()

class ShiftChange(NamedTuple):
    id: int
    dt: datetime
    type = EventType.START_SHIFT

class Sleep(NamedTuple):
    dt: datetime
    type = EventType.FALL_ASLEEP

class Wake(NamedTuple):
    dt: datetime
    type = EventType.WAKE_UP

Event = Union[ShiftChange, Sleep, Wake]

TIME_STR = r'\[(\d{4}-\d{2}-\d{2} \d{2}:\d{2})\]'
START_RE = re.compile(TIME_STR + r' Guard #([^ ]+) begins shift')
SLEEP_RE = re.compile(TIME_STR + r' falls asleep')
WAKE_RE = re.compile(TIME_STR + r' wakes up')
TIME_PSTR = '%Y-%m-%d %H:%M'

def parse(inp: str) -> Event:
    start = START_RE.match(inp)
    sleep = SLEEP_RE.match(inp)
    wake = WAKE_RE.match(inp)

    if start:
        date_str, id = start.groups()
        return ShiftChange(dt=datetime.strptime(date_str, TIME_PSTR), id=id)
    elif sleep:
        [date_str] = sleep.groups()
        return Sleep(dt=datetime.strptime(date_str, TIME_PSTR))
    elif wake:
        [date_str] = wake.groups()
        return Wake(dt=datetime.strptime(date_str, TIME_PSTR))
    else:
        raise Exception(f'Unexpected input: {inp}')

def process(inp: [str]):
    events = sorted((parse(x.strip()) for x in inp), key=lambda x: x.dt)

    sleep_minute_guard = defaultdict(lambda: defaultdict(int))
    current_guard = None
    sleep_start = None

    for event in events:
        if event.type == EventType.START_SHIFT:
            current_guard = event.id
        elif event.type == EventType.FALL_ASLEEP:
            sleep_start = event.dt
        elif event.type == EventType.WAKE_UP:
            sleep_end = event.dt
            dt = sleep_start
            while dt < sleep_end:
                sleep_minute_guard[dt.minute][current_guard] += 1
                dt += timedelta(minutes=1)
        else:
            raise Exception()

    minute_guard_count = [
        (minute, guard, sleep_minute_guard[minute][guard])
        for minute in sleep_minute_guard
        for guard in sleep_minute_guard[minute]
    ]

    highest_minute, highest_guard, count = max(minute_guard_count, key=lambda d: d[2])

    return highest_minute, highest_guard, count, highest_minute * int(highest_guard)

def main():
    import sys
    inps = list(sys.stdin)
    print(process(inps))

if __name__ == '__main__':
    main()