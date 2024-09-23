@echo off
REM Script to compile and run a Spring Boot application

REM Set project directory
SET PROJECT_DIR=C:\Users\ADMIN\Workspace\Practices\spring-jpa-examples

REM Navigate to project directory
cd /d "%PROJECT_DIR%" || (
    echo Failed to change directory to %PROJECT_DIR%
    exit /b 1
)

REM Function to compile the Maven project
:compile_project
echo Compiling the Maven project...
mvn clean compile
IF %ERRORLEVEL% NEQ 0 (
    echo Maven compile failed.
    exit /b 1
)
echo Maven compile successful.

REM Run the Spring Boot application
echo Running the Spring Boot application...
mvn spring-boot:run
IF %ERRORLEVEL% NEQ 0 (
    echo Application failed to start. Retrying Maven compile once more...

    REM Retry compile once if failed
    mvn clean compile
    IF %ERRORLEVEL% NEQ 0 (
        echo Maven compile failed again. Exiting...
        exit /b 1
    )

    echo Maven compile successful on second attempt. Running the application again...
    mvn spring-boot:run
    IF %ERRORLEVEL% NEQ 0 (
        echo Application failed to start again. Exiting...
        exit /b 1
    )
)

echo Application started successfully.
