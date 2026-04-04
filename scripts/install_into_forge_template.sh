#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 1 ]]; then
  echo "Usage: $0 <path-to-forge-1.20-template-root>"
  exit 1
fi

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
target_root="$1"
target_root="$(cd "$target_root" && pwd)"

if [[ ! -d "$target_root" ]]; then
  echo "Target folder does not exist: $target_root"
  exit 1
fi

if [[ "$target_root" == "$repo_root" ]]; then
  echo "Target folder must be different from this repository root: $repo_root"
  exit 1
fi

if [[ ! -d "$target_root/src/main/java" || ! -d "$target_root/src/main/resources" ]]; then
  echo "Target does not look like a Forge template root (missing src/main/java or src/main/resources)."
  exit 1
fi

echo "Installing DominionCore files into: $target_root"

mkdir -p "$target_root/src/main/resources/dominioncore"
mkdir -p "$target_root/src/main/resources/META-INF"
mkdir -p "$target_root/src/test/java"

rsync -a --delete \
  "$repo_root/src/main/resources/dominioncore/" \
  "$target_root/src/main/resources/dominioncore/"

cp "$repo_root/src/main/resources/META-INF/mods.toml" \
   "$target_root/src/main/resources/META-INF/mods.toml"
cp "$repo_root/src/main/resources/pack.mcmeta" \
   "$target_root/src/main/resources/pack.mcmeta"

copy_default_sources() {
  mkdir -p "$target_root/src/main/java/dev/dominioncore"
  rsync -a --delete \
    "$repo_root/src/main/java/dev/dominioncore/" \
    "$target_root/src/main/java/dev/dominioncore/"

  mkdir -p "$target_root/src/test/java/dev/dominioncore"
  rsync -a --delete \
    "$repo_root/src/test/java/dev/dominioncore/" \
    "$target_root/src/test/java/dev/dominioncore/"
}

copy_remapped_tree() {
  local source_root="$1"
  local target_pkg_root="$2"
  local package_name="$3"

  mkdir -p "$target_pkg_root"
  python - "$source_root" "$target_pkg_root" "$package_name" <<'PY'
import os
import re
import sys

source_root, target_root, package_name = sys.argv[1], sys.argv[2], sys.argv[3]
subpackages = [
    "achievement", "app", "bloodline", "client", "combat", "config", "core",
    "dominion", "economy", "faction", "gui", "integration", "io", "leaderboard",
    "mod", "persistence", "progression", "religion", "runtime", "script", "server",
    "sync", "war", "world"
]

java_files = []
for root, _, files in os.walk(source_root):
    for name in files:
        if name.endswith(".java"):
            java_files.append(os.path.join(root, name))

by_basename = {}
for path in java_files:
    base = os.path.basename(path)
    by_basename.setdefault(base, []).append(path)

collisions = {k: v for k, v in by_basename.items() if len(v) > 1}
if collisions:
    print("Cannot flatten sources: duplicate Java basenames detected:")
    for base, files in sorted(collisions.items()):
        print(f"  {base}:")
        for f in files:
            print(f"    - {f}")
    sys.exit(1)

for source_file in java_files:
    with open(source_file, "r", encoding="utf-8") as fh:
        text = fh.read()

    text = re.sub(
        r"^package\s+dev\.dominioncore(?:\.[A-Za-z0-9_.]+)?\s*;",
        f"package {package_name};",
        text,
        flags=re.MULTILINE,
    )
    text = re.sub(r"^\s*import\s+dev\.dominioncore\..*;\s*$", "", text, flags=re.MULTILINE)
    text = text.replace("dev.dominioncore.", "")
    for sub in subpackages:
        text = text.replace(sub + ".", "")
    text = re.sub(r"\n{3,}", "\n\n", text)

    out_path = os.path.join(target_root, os.path.basename(source_file))
    with open(out_path, "w", encoding="utf-8") as out:
        out.write(text)
PY
}

copy_remapped_sources() {
  local package_name="$1"
  local package_path="${package_name//./\/}"
  local target_main_pkg_root="$target_root/src/main/java/$package_path"
  local target_test_pkg_root="$target_root/src/test/java/$package_path"

  copy_remapped_tree "$repo_root/src/main/java/dev/dominioncore" "$target_main_pkg_root" "$package_name"
  copy_remapped_tree "$repo_root/src/test/java/dev/dominioncore" "$target_test_pkg_root" "$package_name"

  rm -rf "$target_root/src/main/java/dev/dominioncore"
  rm -rf "$target_root/src/test/java/dev/dominioncore"
}

example_mod_file="$(find "$target_root/src/main/java" -type f -name "ExampleMod.java" | head -n 1 || true)"
if [[ -n "${example_mod_file:-}" ]]; then
  detected_package="$(sed -n 's/^package[[:space:]]\+//p' "$example_mod_file" | sed 's/;//' | head -n 1)"
  if [[ -n "${detected_package:-}" ]]; then
    copy_remapped_sources "$detected_package"
    target_package_dir="$target_root/src/main/java/${detected_package//./\/}"
    mkdir -p "$target_package_dir"
    sed "s/__PACKAGE__/$detected_package/g" \
      "$repo_root/scripts/templates/ExampleMod.java.template" \
      > "$target_package_dir/ExampleMod.java"
    echo "Updated template ExampleMod.java in package: $detected_package"
    echo "Flattened DominionCore sources into package root: $detected_package (single folder layout)"
  else
    copy_default_sources
  fi
else
  copy_default_sources
fi

echo "Done."
echo "Copied:"
echo "  - Java sources and tests into detected template package (if ExampleMod.java found), otherwise src/main/java/dev/dominioncore/** and src/test/java/dev/dominioncore/**"
echo "  - src/main/resources/dominioncore/**"
echo "  - src/main/resources/META-INF/mods.toml"
echo "  - src/main/resources/pack.mcmeta"
echo "  - Updated template ExampleMod.java content (if found)"
echo
echo "Next steps inside your template folder:"
echo "  1) Update src/main/resources/META-INF/mods.toml (modId, version, displayName, authors)."
echo "  2) Ensure your template dependencies/buildscript match Forge 1.20.x."
echo "  3) Run your normal template command, e.g. ./gradlew runClient."
