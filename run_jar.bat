@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ========================================
echo   HE THONG QUAN LY RAP CHIEU PHIM
echo   (Chay tu file JAR)
echo ========================================
echo.

java -jar CinemaManagement.jar

pause
