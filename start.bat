@echo off
setlocal
cd /d "%~dp0"

set "JAVA_HOME=%~dp0.tools\jdk-25"
if not exist "%JAVA_HOME%\bin\java.exe" (
    set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
)
set "PATH=%JAVA_HOME%\bin;%~dp0.tools\apache-maven-3.9.6\bin;%~dp0.tools\pgsql\bin;%PATH%"

echo ===================================================
echo Starting TrainTkt (Spring Boot + PostgreSQL + Java 25)
echo ===================================================

pg_isready -h localhost -p 5432 >nul 2>&1
if errorlevel 1 (
    echo Starting local PostgreSQL server...
    pg_ctl -D "%~dp0data\postgres" -l "%~dp0data\postgres.log" start
    timeout /t 2 /nobreak >nul
)

echo Building and running Spring Boot application...
mvn spring-boot:run
