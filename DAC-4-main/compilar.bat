@echo off
setlocal
cd /d "%~dp0"
set "DAC_JAVA="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "DAC_JAVA=%JAVA_HOME%\bin\java.exe"
if not defined DAC_JAVA for %%J in (java.exe) do if not "%%~$PATH:J"=="" set "DAC_JAVA=%%~$PATH:J"
if not defined DAC_JAVA for /d %%D in ("%USERPROFILE%\.jdks\*") do if exist "%%~fD\bin\java.exe" set "DAC_JAVA=%%~fD\bin\java.exe"
if not defined DAC_JAVA (
    echo JDK nao encontrado. Configure Java 17 ou superior.
    pause
    exit /b 1
)
if not exist build\classes mkdir build\classes
(for /r src %%F in (*.java) do @echo "%%F") > build\fontes.txt
"%DAC_JAVA%" -m jdk.compiler/com.sun.tools.javac.Main --release 17 -encoding UTF-8 -cp "lib/*" -d build/classes @build/fontes.txt
if errorlevel 1 (
    pause
    exit /b 1
)
"%DAC_JAVA%" -m jdk.jartool/sun.tools.jar.Main --create --file dac.jar --manifest manifest.mf -C build/classes .
if errorlevel 1 (
    pause
    exit /b 1
)
echo Compilacao concluida. Execute executar.bat.
pause
