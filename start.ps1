Set-Location $PSScriptRoot

$env:JAVA_HOME = "$PSScriptRoot\.tools\jdk-25"
if (-not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
}
$env:Path = "$env:JAVA_HOME\bin;$PSScriptRoot\.tools\apache-maven-3.9.6\bin;$PSScriptRoot\.tools\pgsql\bin;$env:Path"

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "Starting TrainTkt (Spring Boot + PostgreSQL + Java 25)" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

# Ensure PostgreSQL is running
& pg_isready -h localhost -p 5432 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Starting local PostgreSQL server..." -ForegroundColor Yellow
    & pg_ctl -D "$PSScriptRoot\data\postgres" -l "$PSScriptRoot\data\postgres.log" start
    Start-Sleep -Seconds 2
}

& mvn spring-boot:run
