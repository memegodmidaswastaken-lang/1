#!/usr/bin/env bash
set -euo pipefail

# Ensure production code has no JUnit dependency leakage.
if rg -n "org\.junit|@Test" src/main/java >/dev/null; then
  echo "ERROR: JUnit imports/annotations found in src/main/java"
  rg -n "org\.junit|@Test" src/main/java
  exit 1
fi

# Ensure critical source files exist in expected locations.
for f in \
  src/main/java/dev/dominioncore/core/ScalingFormula.java \
  src/test/java/dev/dominioncore/core/ScalingFormulaTest.java \
  src/main/java/dev/dominioncore/dominion/Dominion.java \
  src/main/java/dev/dominioncore/app/SeedData.java \
  src/main/java/dev/dominioncore/io/JsonSeedLoader.java
do
  if [[ ! -f "$f" ]]; then
    echo "ERROR: Missing required file: $f"
    exit 1
  fi
done

echo "Layout check passed."
