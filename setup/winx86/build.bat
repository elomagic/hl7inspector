@ECHO OFF 
TITLE Build HL7 Inspector Setup

IF "%NSIS_FOLDER%"=="" (
	ECHO Environmental variable NSIS_FOLDER not set. Please check system configuration
	ECHO.
	PAUSE
	EXIT /B
)

SET /p APP_VERSION=<..\..\version.number

IF "%APP_VERSION%"=="" (
	ECHO Unable to determine version of HL7 Inspector
	ECHO.
	PAUSE
    EXIT /B
)

TITLE Build HL7 Inspector Setup Version %APP_VERSION%
ECHO Prepare build process
ECHO.
RMDIR /S /Q dist
MKDIR dist
RMDIR /S /Q src
MKDIR src
XCOPY ..\src\*.* .\src /E /R /Y
COPY ..\application.ico .\src 
ECHO.
REM ECHO ===========================================================
REM ECHO.
REM ECHO Create Java Launcher
REM ECHO.
REM "%LAUNCH4J_FOLDER%\launch4jc.exe" "conf\launch4j-setup.xml"
REM ECHO.
ECHO ===========================================================
ECHO.
ECHO Build Setup
ECHO.
"%NSIS_FOLDER%\makensis.exe" /DAPP_VERSION=%APP_VERSION% "conf\hl7inspector.nsi"
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