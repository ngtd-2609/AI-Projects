@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ========================================
echo   HE THONG QUAN LY RAP CHIEU PHIM
echo ========================================
echo.

echo Dang bien dich...
if not exist out mkdir out

javac -encoding UTF-8 -cp "lib/*" -d out src/model/*.java src/dao/*.java src/controller/*.java src/view/*.java src/view/components/*.java src/util/*.java src/Main.java

if %errorlevel% neq 0 (
    echo.
    echo LOI: Bien dich that bai!
    pause
    exit /b 1
)

echo Bien dich thanh cong!
echo.
echo Dang khoi chay ung dung...
echo.

java -cp "out;lib/*" Main

pause