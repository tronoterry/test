"""Generates simple solid-color rounded-square PNG launcher icons.

Run once during development; the resulting PNGs are committed to the repo
so the Android build doesn't depend on any image-processing library.
"""
import os
import struct
import zlib

SIZES = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}

BG = (37, 99, 235, 255)  # #2563EB
FG = (255, 255, 255, 255)

ROOT = os.path.join(os.path.dirname(__file__), "..", "android", "app", "src", "main", "res")


def png_chunk(tag, data):
    return (
        struct.pack(">I", len(data))
        + tag
        + data
        + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)
    )


def make_icon(size):
    pixels = bytearray()
    cx = cy = size / 2
    r_outer = size * 0.46
    # A simple envelope-ish mark: rounded square background + a smaller
    # white rounded rectangle "letter" shape near the center.
    letter_w = size * 0.46
    letter_h = size * 0.32
    lx0, ly0 = cx - letter_w / 2, cy - letter_h / 2
    lx1, ly1 = cx + letter_w / 2, cy + letter_h / 2

    for y in range(size):
        row = bytearray()
        for x in range(size):
            dx, dy = x - cx + 0.5, y - cy + 0.5
            dist = (dx * dx + dy * dy) ** 0.5
            if dist <= r_outer:
                if lx0 <= x < lx1 and ly0 <= y < ly1:
                    color = FG
                else:
                    color = BG
            else:
                color = (0, 0, 0, 0)
            row.extend(color)
        pixels.append(0)  # filter type 0 for this scanline
        pixels.extend(row)

    raw = bytes(pixels)
    compressed = zlib.compress(raw, 9)

    sig = b"\x89PNG\r\n\x1a\n"
    ihdr = struct.pack(">IIBBBBB", size, size, 8, 6, 0, 0, 0)
    return (
        sig
        + png_chunk(b"IHDR", ihdr)
        + png_chunk(b"IDAT", compressed)
        + png_chunk(b"IEND", b"")
    )


def main():
    for folder, size in SIZES.items():
        out_dir = os.path.join(ROOT, folder)
        os.makedirs(out_dir, exist_ok=True)
        out_path = os.path.join(out_dir, "ic_launcher.png")
        with open(out_path, "wb") as f:
            f.write(make_icon(size))
        print(f"wrote {out_path} ({size}x{size})")


if __name__ == "__main__":
    main()
