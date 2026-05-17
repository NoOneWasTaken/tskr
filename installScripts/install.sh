#!/bin/bash

set -e

REPO="NoOneWasTaken/tskr"
INSTALL_DIR="/usr/local/tskr"
BIN_DIR="/usr/local/bin"

echo "[tskr] Downloading latest release..."
curl -L "https://github.com/$REPO/releases/latest/download/tskr.zip" -o /tmp/tskr.zip

echo "[tskr] Installing..."
sudo rm -rf "$INSTALL_DIR"
sudo mkdir -p "$INSTALL_DIR"
sudo unzip -q /tmp/tskr.zip -d /tmp/tskr_extracted
sudo cp -r /tmp/tskr_extracted/tskr-*/* "$INSTALL_DIR"
rm -rf /tmp/tskr.zip /tmp/tskr_extracted

echo "[tskr] Adding to PATH..."
sudo ln -sf "$INSTALL_DIR/bin/tskr" "$BIN_DIR/tskr"

echo "[tskr] Done! Run 'tskr --help' to get started."
