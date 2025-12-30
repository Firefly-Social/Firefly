#!/usr/bin/env bash

# Fail if any commands fails.
set -e


CATALOG="gradle/libs.versions.toml"

# usage: findVersion <VERSION_NAME>
# Looks for the given version in Gradle Version Catalog (libs.versions.toml file).
findVersion() {
  local name="$1"
  local version

  # Match lines like: name = "1.2.3" (allow whitespace)
  version="$(sed -nE "s/^[[:space:]]*$name[[:space:]]*=[[:space:]]*\"([^\"]+)\".*/\1/p" "$CATALOG" | head -n1)"

  if [[ -z "${version:-}" ]]; then
    echo "ERROR: $name version not found in $CATALOG" >&2
    exit 1
  fi

  RETURN="$version"
}

semver_major_minor() {
  # Extract "X.Y" from "X.Y.Z" (or "X.Y")
  local v="$1"
  if [[ "$v" =~ ^([0-9]+)\.([0-9]+) ]]; then
    echo "${BASH_REMATCH[1]}.${BASH_REMATCH[2]}"
  else
    echo ""
  fi
}

findVersion "kotlin-ksp"
KSP_VERSION="$RETURN"
findVersion "kotlin"
KOTLIN_VERSION="$RETURN"

KOTLIN_MM="$(semver_major_minor "$KOTLIN_VERSION")"
KSP_MM="$(semver_major_minor "$KSP_VERSION")"

if [[ -z "$KOTLIN_MM" ]]; then
  echo "ERROR: Kotlin version '$KOTLIN_VERSION' is not in a recognized numeric format" >&2
  exit 1
fi
if [[ -z "$KSP_MM" ]]; then
  echo "ERROR: KSP version '$KSP_VERSION' is not in a recognized numeric format" >&2
  exit 1
fi

# Old KSP format: "<kotlinVersion>-<kspVersion>" (e.g. 2.0.20-1.0.25)
if [[ "$KSP_VERSION" == *-* ]]; then
  if [[ "$KSP_VERSION" =~ ^${KOTLIN_VERSION}- ]]; then
    echo "OK: KSP ($KSP_VERSION) matches Kotlin ($KOTLIN_VERSION) [old format]"
    exit 0
  fi

  echo "ERROR: KSP version ($KSP_VERSION) doesn't match Kotlin version ($KOTLIN_VERSION) [old format expects prefix '${KOTLIN_VERSION}-']" >&2
  exit 1
fi

# New KSP format: "X.Y.Z" (e.g. 2.3.4) — require major.minor match Kotlin's major.minor (2.3)
if [[ "$KSP_MM" == "$KOTLIN_MM" ]]; then
  echo "OK: KSP ($KSP_VERSION) matches Kotlin ($KOTLIN_VERSION) [new format major.minor]"
  exit 0
fi

echo "ERROR: KSP version ($KSP_VERSION) doesn't match Kotlin version ($KOTLIN_VERSION) [new format expects major.minor '${KOTLIN_MM}.*']" >&2
exit 1
