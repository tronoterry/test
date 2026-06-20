#!/usr/bin/env python3
"""
Polls the Reddit app's UI via `adb shell uiautomator dump` and swipes the
feed past any post card labeled Ad / Promoted / Sponsored - a no-build,
no-install alternative to the AdShield accessibility-service app in this repo.

Setup (entirely on-device, no PC required):
  1. Install Termux from F-Droid (not the outdated Play Store build).
  2. pkg install android-tools python
  3. Settings -> System -> Developer options -> Wireless debugging -> on.
     Tap "Pair device with pairing code" to get a pairing port + code, and
     note the separate port shown on the main Wireless debugging screen.
  4. adb pair localhost:<pairing_port>      (enter the 6-digit code)
     adb connect localhost:<connect_port>
  5. python reddit_ad_skip.py

Stop with Ctrl+C. Logic mirrors app/src/main/java/com/adshield/redditblock/
RedditAdBlockerService.kt: find a node whose text is an ad marker, walk up
to the ancestor whose parent is scrollable (the post card), then swipe the
feed up by that card's height.
"""
import re
import subprocess
import sys
import time
import xml.etree.ElementTree as ET

REDDIT_PACKAGE = "com.reddit.frontpage"
DUMP_PATH = "/sdcard/window_dump.xml"
POLL_INTERVAL_SEC = 1.0
POST_SWIPE_PAUSE_SEC = 1.5
AD_MARKERS = {"Ad", "Promoted", "Sponsored"}
BOUNDS_RE = re.compile(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]")


def adb(*args: str) -> str:
    result = subprocess.run(["adb", *args], capture_output=True, text=True, timeout=10)
    return result.stdout


def foreground_package() -> str:
    out = adb("shell", "dumpsys", "window", "windows")
    match = re.search(r"mCurrentFocus.*?\{.*?\s([\w.]+)/", out)
    return match.group(1) if match else ""


def screen_width() -> int:
    out = adb("shell", "wm", "size")
    match = re.search(r"(\d+)x(\d+)", out)
    if not match:
        raise RuntimeError(f"could not parse screen size from: {out!r}")
    return int(match.group(1))


def dump_ui_tree() -> ET.Element:
    adb("shell", "uiautomator", "dump", DUMP_PATH)
    return ET.fromstring(adb("shell", "cat", DUMP_PATH))


def parse_bounds(bounds_attr: str) -> tuple[int, int, int, int]:
    match = BOUNDS_RE.match(bounds_attr or "")
    if not match:
        return (0, 0, 0, 0)
    left, top, right, bottom = (int(g) for g in match.groups())
    return left, top, right, bottom


def find_marker_node(node: ET.Element, parent_of: dict) -> ET.Element | None:
    for child in node:
        parent_of[child] = node
        if child.get("text", "").strip() in AD_MARKERS:
            return child
        found = find_marker_node(child, parent_of)
        if found is not None:
            return found
    return None


def find_card_bounds(marker: ET.Element, parent_of: dict) -> tuple[int, int, int, int] | None:
    current = marker
    while current in parent_of:
        parent = parent_of[current]
        if parent.get("scrollable") == "true":
            return parse_bounds(current.get("bounds", ""))
        current = parent
    return None


def swipe_past(card_bounds: tuple[int, int, int, int], width: int) -> None:
    _, top, _, bottom = card_bounds
    center_x = width // 2
    start_y = bottom
    end_y = max(50, top - 40)
    print(f"skipping ad: swipe {center_x},{start_y} -> {center_x},{end_y}")
    adb("shell", "input", "swipe", str(center_x), str(start_y), str(center_x), str(end_y), "120")


def main() -> None:
    width = screen_width()
    print(f"watching {REDDIT_PACKAGE} every {POLL_INTERVAL_SEC}s, Ctrl+C to stop")
    while True:
        try:
            if foreground_package() == REDDIT_PACKAGE:
                parent_of: dict = {}
                marker = find_marker_node(dump_ui_tree(), parent_of)
                card_bounds = find_card_bounds(marker, parent_of) if marker is not None else None
                if card_bounds is not None:
                    swipe_past(card_bounds, width)
                    time.sleep(POST_SWIPE_PAUSE_SEC)
        except (subprocess.TimeoutExpired, ET.ParseError, RuntimeError) as exc:
            print(f"warning: {exc}", file=sys.stderr)
        time.sleep(POLL_INTERVAL_SEC)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\nstopped")
