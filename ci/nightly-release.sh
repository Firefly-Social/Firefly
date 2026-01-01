#!/usr/bin/env bash

# Fail if any commands fails.
set -e

RELEASE_VERSION_CODE=$1

KEY_STORE="secrets/firefly.jks"

if [[ ! -f "$KEY_STORE" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Bumping version code to ${RELEASE_VERSION_CODE}…"
ci/set-version-code.sh "$RELEASE_VERSION_CODE"

VERSION_NAME=$(cat app/build.gradle.kts | grep versionName | cut -d "\"" -f2)
TAG="$VERSION_NAME.$RELEASE_VERSION_CODE"
RELEASE_NAME="Nightly $TAG"

ci/nightly-build.sh

APK_FILE="secrets/nightly.apk"

if [[ -z "$APK_FILE" ]]; then
  echo "No APK found"
  exit 1
fi

echo "Found APK: ${APK_FILE}"

if [[ -z "${GH_TOKEN:-}" ]]; then
  echo "GH_TOKEN is not set"
  echo "In GitHub Actions, set: GH_TOKEN: \${{ secrets.GITHUB_TOKEN }}"
  exit 1
fi

echo "Publishing GitHub release ${RELEASE_NAME} (${TAG})…"

if gh release view "$TAG" >/dev/null 2>&1; then
  echo "Release already exists, replacing APK…"
  gh release upload "$TAG" "$APK_FILE" --clobber
else
  gh release create "$TAG" \
    --title "$RELEASE_NAME" \
    --generate-notes \
    "$APK_FILE"
fi