#!/bin/bash

set -e

REPO="NoOneWasTaken/tskr"
BIN_DIR="/usr/local/bin"
TMP_FILE="/tmp/tskr"
BACKUP_FILE="/tmp/tskr.bak"

# Detect platform
if [[ "$OSTYPE" == "darwin"* ]]; then
    ASSET="tskr-mac"
else
    ASSET="tskr-linux"
fi

# Backup existing binary if it exists
if [ -f "$BIN_DIR/tskr" ]; then
    sudo cp "$BIN_DIR/tskr" "$BACKUP_FILE"
fi

rollback() {
    echo "[tskr] Something went wrong. Rolling back..."
    sudo rm -f "$TMP_FILE"
    if [ -f "$BACKUP_FILE" ]; then
        sudo mv "$BACKUP_FILE" "$BIN_DIR/tskr"
        echo "[tskr] Restored previous version."
    else
        sudo rm -f "$BIN_DIR/tskr"
        echo "[tskr] No previous version to restore. Cleaned up."
    fi
    exit 1
}

trap rollback ERR

echo "[tskr] Downloading latest release..."
curl -fL "https://github.com/$REPO/releases/latest/download/$ASSET" -o "$TMP_FILE"

echo "[tskr] Installing..."
sudo mv "$TMP_FILE" "$BIN_DIR/tskr"
sudo chmod +x "$BIN_DIR/tskr"

# Verify binary works
if ! "$BIN_DIR/tskr" --help > /dev/null 2>&1; then
    rollback
fi

# Cleanup backup
sudo rm -f "$BACKUP_FILE"

echo "[tskr] Done! Run 'tskr --help' to get started."