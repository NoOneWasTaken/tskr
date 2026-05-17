# Run as Administrator
if (-NOT ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "[ERROR] Please run this script as Administrator." -ForegroundColor Red
    exit 1
}

$repo = "NoOneWasTaken/tskr"
$installPath = "C:\Program Files\tskr"
$zipPath = "$env:TEMP\tskr.zip"
$extractPath = "$env:TEMP\tskr_extracted"

Write-Host "[tskr] Downloading latest release..." -ForegroundColor Cyan
Invoke-WebRequest -Uri "https://github.com/$repo/releases/latest/download/tskr.zip" -OutFile $zipPath

Write-Host "[tskr] Installing..." -ForegroundColor Cyan
if (Test-Path $installPath) { Remove-Item -Recurse -Force $installPath }
Expand-Archive -Path $zipPath -DestinationPath $extractPath -Force
$extracted = Get-ChildItem $extractPath | Select-Object -First 1
if (Test-Path $installPath) { Remove-Item -Recurse -Force $installPath }
Move-Item "$extractPath\$($extracted.Name)" $installPath
Remove-Item -Recurse -Force $zipPath, $extractPath

Write-Host "[tskr] Adding to PATH..." -ForegroundColor Cyan
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
if ($currentPath -notlike "*tskr*") {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$installPath\bin", "Machine")
}

Write-Host "[tskr] Done! Restart your terminal and run 'tskr --help' to get started." -ForegroundColor Green
