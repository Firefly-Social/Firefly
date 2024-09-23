#!/usr/bin/env bash

# Fail if any commands fails.
set -e

SIGNED_APK="app/build/outputs/apk/nightly/app-nightly.apk"
SIGNED_APK_FINAL_PATH="secrets/nightly.apk"

BUILD_TOOLS_DIR="$ANDROID_HOME/build-tools/33.0.1"
KEY_STORE="secrets/firefly.jks"

if [[ ! -f "$KEY_STORE" ]]; then
  echo "Secrets not decrypted"
  exit 1
fi

echo "Building APK"
./gradlew :app:assembleNightly

mv $SIGNED_APK $SIGNED_APK_FINAL_PATH

echo "Done!"
echo "The signed APK is at $SIGNED_APK_FINAL_PATH"
