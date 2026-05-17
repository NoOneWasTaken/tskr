# Run as Administrator
if (-NOT ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "[ERROR] Please run this script as Administrator." -ForegroundColor Red
    exit 1
}

Write-Host "[tskr] Building..." -ForegroundColor Cyan
./gradlew installDist

Write-Host "[tskr] Installing..." -ForegroundColor Cyan
$installPath = "C:\Program Files\tskr"
if (Test-Path $installPath) {
    Remove-Item -Recurse -Force $installPath
}
Copy-Item -Recurse "binary\tskr" "C:\Program Files\tskr"

Write-Host "[tskr] Adding to PATH..." -ForegroundColor Cyan
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
if ($currentPath -notlike "*tskr*") {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;C:\Program Files\tskr\bin", "Machine")
}

Write-Host "[tskr] Done! Restart your terminal and run 'tskr --help' to get started." -ForegroundColor Green
