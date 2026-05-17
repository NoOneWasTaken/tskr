#!/bin/bash

set -e

echo "[tskr] Building..."
./gradlew installDist

echo "[tskr] Installing..."
sudo cp -r binary/tskr /usr/local/tskr

echo "[tskr] Adding to PATH..."
sudo ln -sf /usr/local/tskr/bin/tskr /usr/local/bin/tskr

echo "[tskr] Done! Run 'tskr --help' to get started."
