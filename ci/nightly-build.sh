#!/usr/bin/env bash

# Fail if any commands fails.
set -e

APK_OUTPUT_DIR="app/build/outputs/apk/nightly"
UNSIGNED_APK="$APK_OUTPUT_DIR/app-nightly-unsigned.apk"
ALIGNED_APK="$APK_OUTPUT_DIR/nightly-aligned.apk"
SIGNED_APK="$APK_OUTPUT_DIR/nightly.apk"
SIGNED_APK_FINAL_PATH="secrets/nightly.apk"

BUILD_TOOLS_DIR="$ANDROID_HOME/build-tools/33.0.1"
KEY_STORE="secrets/firefly.jks"

if [[ ! -f "$SECRET_ENV" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Initializing secrets…"
source "secrets/secret-environment-variables.sh"

echo "Building APK"
./gradlew :app:assembleNightly

mv $SIGNED_APK $SIGNED_APK_FINAL_PATH

echo "Done!"
echo "The signed APK is at $SIGNED_APK_FINAL_PATH"
