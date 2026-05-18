#!/bin/bash

set -e

REPO="NoOneWasTaken/tskr"
BIN_DIR="/usr/local/bin"

# Detect platform
if [[ "$OSTYPE" == "darwin"* ]]; then
    ASSET="tskr-mac"
else
    ASSET="tskr-linux"
fi

echo "[tskr] Downloading latest release..."
curl -L "https://github.com/$REPO/releases/latest/download/$ASSET" -o /tmp/tskr

echo "[tskr] Installing..."
sudo mv /tmp/tskr "$BIN_DIR/tskr"
sudo chmod +x "$BIN_DIR/tskr"

echo "[tskr] Done! Run 'tskr --help' to get started."