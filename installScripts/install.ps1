# Run as Administrator
if (-NOT ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "[ERROR] Please run this script as Administrator." -ForegroundColor Red
    exit 1
}

$repo = "NoOneWasTaken/tskr"
$installPath = "C:\Program Files\tskr"
$exePath = "$env:TEMP\tskr.exe"
$backupPath = "$env:TEMP\tskr.bak.exe"

function Rollback {
    Write-Host "[tskr] Something went wrong. Rolling back..." -ForegroundColor Yellow
    Remove-Item -Force $exePath -ErrorAction SilentlyContinue
    if (Test-Path $backupPath) {
        if (-Not (Test-Path $installPath)) { New-Item -ItemType Directory -Force -Path $installPath | Out-Null }
        Move-Item $backupPath "$installPath\tskr.exe" -Force
        Write-Host "[tskr] Restored previous version." -ForegroundColor Yellow
    } else {
        Remove-Item -Recurse -Force $installPath -ErrorAction SilentlyContinue
        Write-Host "[tskr] No previous version to restore. Cleaned up." -ForegroundColor Yellow
    }
    exit 1
}

# Backup existing binary if it exists
if (Test-Path "$installPath\tskr.exe") {
    Copy-Item "$installPath\tskr.exe" $backupPath -Force
}

try {
    Write-Host "[tskr] Downloading latest release..." -ForegroundColor Cyan
    Invoke-WebRequest -Uri "https://github.com/$repo/releases/latest/download/tskr.exe" -OutFile $exePath -ErrorAction Stop

    Write-Host "[tskr] Installing..." -ForegroundColor Cyan
    if (Test-Path $installPath) { Remove-Item -Recurse -Force $installPath }
    New-Item -ItemType Directory -Force -Path $installPath | Out-Null
    Move-Item $exePath "$installPath\tskr.exe" -Force

    # Add to PATH if not already there
    Write-Host "[tskr] Adding to PATH..." -ForegroundColor Cyan
    $currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
    if ($currentPath -notlike "*tskr*") {
        [Environment]::SetEnvironmentVariable("Path", "$currentPath;$installPath", "Machine")
    }

    # Verify binary works
    $result = & "$installPath\tskr.exe" --help 2>&1
    if ($LASTEXITCODE -ne 0) { Rollback }

    # Cleanup backup
    Remove-Item -Force $backupPath -ErrorAction SilentlyContinue

    Write-Host "[tskr] Done! Restart your terminal and run 'tskr --help' to get started." -ForegroundColor Green

} catch {
    Write-Host "[ERROR] $($_.Exception.Message)" -ForegroundColor Red
    Rollback
}