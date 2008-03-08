# Install / Uninstall script for the elomagic HL7 Inspector
# Copyright 2008 Carsten Rambow / elomagic
# Author: Carsten Rambow

# Uncomment in test mode.  
SetCompressor /SOLID lzma

# Include Modern UI
!include "MUI.nsh"
    
# General

!define APPLICATION_VERSION "1.2.1.1"
!define APPLICATION_NAME "hl7inspector.exe"
!define REG_KEY_PATH "Software\elomagic\hl7inspector"

# Name and file
Name "elomagic HL7 Inspector"
OutFile "dist\HL7Inspector-${APPLICATION_VERSION}-Setup.exe"

# Default installation folder
InstallDir "$PROGRAMFILES\HL7 Inspector"

# Get installation folder from registry if available
InstallDirRegKey HKLM "${REG_KEY_PATH}" ""
  
# Icon of Setup
!define MUI_ICON "application.ico"
!define MUI_UNICON "application.ico"

RequestExecutionLevel user
  
# Interface Settings
!define MUI_ABORTWARNING
!define MUI_FINISHPAGE_RUN $INSTDIR\bin\${APPLICATION_NAME}
!define MUI_FINISHPAGE_RUN_TEXT "Run HL7 Inspector"

# Pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE ".\src\license\license-gpl.txt"
;  !insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

# Languages
# First language is the default language
!insertmacro MUI_LANGUAGE "English"  

# Additional Setup Version Information

VIProductVersion "${APPLICATION_VERSION}"
  
VIAddVersionKey "ProductName" "HL7 Inspector"
VIAddVersionKey "Comments" ""
VIAddVersionKey "CompanyName" "elomagic"
# VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalTrademarks" "Test Application is a trademark of Fake company"
VIAddVersionKey "LegalCopyright" "elomagic"
VIAddVersionKey "FileDescription" "elomagic HL7 Inspector"
VIAddVersionKey "FileVersion" "${APPLICATION_VERSION}"
  
;--------------------------------
;Installer Sections

Section "!Main" SecCore
	SectionIn 1 2 RO
	SetOutPath "$INSTDIR"

	# ADD YOUR OWN FILES HERE...
	File /r /x ".svn" "src\*.*"

	# Store installation folder
	WriteRegStr HKLM "${REG_KEY_PATH}" "" $INSTDIR
	WriteRegStr HKLM "${REG_KEY_PATH}" "version" ${APPLICATION_VERSION}	
    
	# Create uninstaller
	WriteUninstaller "$INSTDIR\Uninstall.exe"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "DisplayName" "HL7 Inspector"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "UninstallString" "$INSTDIR\Uninstall.exe"  
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "DisplayIcon" "$INSTDIR\Uninstall.exe"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "DisplayVersion" "${APPLICATION_VERSION}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "Installation" "$INSTDIR"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "Publisher" "elomagic"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "URLUpdateInfo" "http://www.elomagic.de"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "URLInfoAbout" "http://www.elomagic.de"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "RegCompany" "elomagic"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "RegOwner" "elomagic"
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "NoRepair" 1

	# Create shortcuts
	CreateDirectory "$SMPROGRAMS\HL7 Inspector"
	CreateShortCut "$SMPROGRAMS\HL7 Inspector\HL7 Inspector.lnk" "$INSTDIR\bin\${APPLICATION_NAME}" "-once" "$INSTDIR\bin\${APPLICATION_NAME}"
	CreateShortCut "$SMPROGRAMS\HL7 Inspector\Samples.lnk" "$INSTDIR\bin\${APPLICATION_NAME}" "-localweb" "$INSTDIR\bin\${APPLICATION_NAME}"
	CreateShortCut "$SMPROGRAMS\HL7 Inspector\Uninstall.lnk" "$INSTDIR\Uninstall.exe" "" "$INSTDIR\Uninstall.exe"   
  
	# Setup XP firewall
	DetailPrint "Setup firewall"
	ExecCmd::exec /NOUNLOAD /TIMEOUT=2000 'netsh firewall add allowedprogram "$INSTDIR\hl7inspector.exe" "HL7 Inspector" ENABLE' ""
	Pop $0 # thread handle for wait
SectionEnd

# Uninstaller Section

Section "Uninstall"
	RMDir /r /REBOOTOK "$SMPROGRAMS\HL7 Inspector"
	Delete "$SMPROGRAMS\HL7 Inspector\HL7 Inspector.lnk"
	Delete "$SMPROGRAMS\HL7 Inspector\Samples.lnk"
	Delete "$SMPROGRAMS\HL7 Inspector\Uninstall.lnk"
  
	RMDir "$SMPROGRAMS\HL7 Inspector"  
  
	RMDir /r /REBOOTOK "$INSTDIR"

	DeleteRegKey /ifempty HKLM "Software\elomagic\hl7inspector"
  
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector"
SectionEnd

Function .onInit 
	GetVersion::WindowsVersion
	Pop $R0
  
	IntCmpU $R0 5 +3 +1 +3
	MessageBox MB_ICONSTOP "Microsoft Windows 2000 or higher is required.$\n$\nMicrosoft Windows NT/95/98/ME not supported."
	Abort   

FunctionEnd