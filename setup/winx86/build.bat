@ECHO OFF 
TITLE Build elomagic HL7 Inspector Setup
SET /p APP_VERSION=<version.txt
TITLE Build elomagic HL7 Inspector Setup Version %APP_VERSION%
"%NSIS_FOLDER%\makensis.exe" /DAPP_VERSION=%APP_VERSION% "hl7inspector.nsi"
ECHO ===========================================================
REM ECHO.
REM ECHO Signing Setup
REM SET /p CERT_PASSWORD=Certificate Password:
REM ".\tools\signtool.exe" sign /f ".\tools\elomagic.pfx" /p %CERT_PASSWORD% /du www.elomagic.de ".\dist\elomagic HL7Inspector %APP_VERSION% Setup.exe"
REM ECHO.
REM ECHO ===========================================================
ECHO.
ECHO Build of setup version %APP_VERSION% finished. Check results
ECHO.
pause