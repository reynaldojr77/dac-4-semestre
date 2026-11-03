@echo off
setlocal
cd /d "%~dp0"
set "DAC_JAVA="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "DAC_JAVA=%JAVA_HOME%\bin\java.exe"
if not defined DAC_JAVA for %%J in (java.exe) do if not "%%~$PATH:J"=="" set "DAC_JAVA=%%~$PATH:J"
if not defined DAC_JAVA for /d %%D in ("%USERPROFILE%\.jdks\*") do if exist "%%~fD\bin\java.exe" set "DAC_JAVA=%%~fD\bin\java.exe"
if not defined DAC_JAVA (
    echo Java nao encontrado. Instale Java 17 ou superior ou execute Main.java pelo IntelliJ.
    pause
    exit /b 1
)
"%DAC_JAVA%" --enable-native-access=ALL-UNNAMED -jar "%~dp0dac.jar" %*
set "DAC_EXIT=%ERRORLEVEL%"
if not "%DAC_EXIT%"=="0" (
    echo.
    echo Falha na execucao. Confira a mensagem acima e o README.
    pause
)
exit /b %DAC_EXIT%
