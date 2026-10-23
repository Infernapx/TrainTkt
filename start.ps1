$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$PSScriptRoot\.tools\apache-maven-3.9.6\bin;$env:PATH"

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "Starting Train Ticket Reservation System on http://localhost:8080" -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Cyan

if (-not (Test-Path "$PSScriptRoot\target\dependency\webapp-runner.jar")) {
    Write-Host "Building project..." -ForegroundColor Yellow
    mvn clean package -DskipTests
}

java -jar "$PSScriptRoot\target\dependency\webapp-runner.jar" --port 8080 "$PSScriptRoot\target\TrainBook-1.0.0-SNAPSHOT.war"
