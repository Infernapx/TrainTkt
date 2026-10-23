@echo off
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
set "PATH=%JAVA_HOME%\bin;%~dp0.tools\apache-maven-3.9.6\bin;%PATH%"

echo ===================================================
echo Starting Train Ticket Reservation System on http://localhost:8080
echo ===================================================

if not exist "%~dp0target\dependency\webapp-runner.jar" (
    echo Building project...
    call mvn clean package -DskipTests
)

java -jar "%~dp0target\dependency\webapp-runner.jar" --port 8080 "%~dp0target\TrainBook-1.0.0-SNAPSHOT.war"
