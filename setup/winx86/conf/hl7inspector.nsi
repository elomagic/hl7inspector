# elomagic HL7 Inspector Setup Script
# Written by Carsten Rambow
# (c)2010 Carsten Rambow, Roederheim-Gronau, Germany

!addincludedir $%NSIS_INCLUDE%
!addplugindir $%NSIS_PLUGINS%

# --------------------------------
# General

#!define APP_VERSION "1.0.0.9"
!define APP_NAME "HL7 Inspector"
!define APP_MANUFACTOR "elomagic"
!define APP_MAIN "hl7inspector.jar"

!define APP_REGKEY "Software\${APP_MANUFACTOR}\${APP_NAME}" 
!define APP_UNINSTALL "Uninstall ${APP_MANUFACTOR} ${APP_NAME}.exe"

!include "nsDialogs.nsh"
!include "elomagicLF.nsh"

# Name and file
Name "${APP_MANUFACTOR} ${APP_NAME} ${APP_VERSION}"
OutFile "..\dist\${APP_MANUFACTOR} ${APP_NAME} ${APP_VERSION} Setup.exe"

# Default installation folder
InstallDir "$PROGRAMFILES\${APP_MANUFACTOR}\${APP_NAME}"
  
# Get installation folder from registry if available
InstallDirRegKey HKLM "${APP_REGKEY}" ""

# Request application privileges for Windows Vista
RequestExecutionLevel admin

# Custom page
# Var DonateDialog
Var hwnd
#Var Hwd_RunApp

# --------------------------------
# Interface Settings

!define MUI_ABORTWARNING
!define MUI_FINISHPAGE_RUN "$SYSDIR\javaw.exe" 
!define MUI_FINISHPAGE_RUN_PARAMETERS "-jar $\"$INSTDIR\${APP_MAIN}$\""

# --------------------------------
# Pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "..\src\license\license-gpl.txt"
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
Page Custom DonatePage DonatePageLeave  
!insertmacro MUI_PAGE_FINISH
  
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
  
  
# --------------------------------
# Languages
 
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "German"

;--------------------------------
;Installer Sections

Section "${APP_NAME} ${APP_VERSION}" SecDummy
	SectionIn RO
  SetOutPath "$INSTDIR"
  
  ;ADD YOUR OWN FILES HERE...
  File /r /x ".svn" "..\src\*.*"
  
    ; Store installation folder
    WriteRegStr HKLM "${APP_REGKEY}" "" $INSTDIR

    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "DisplayName" "${APP_MANUFACTOR} ${APP_NAME}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "UninstallString" '"$INSTDIR\${APP_UNINSTALL}"'
    WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "NoModify" 1
    WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "NoRepair" 1
  
    # Create uninstaller
    WriteUninstaller "$INSTDIR\${APP_UNINSTALL}"
  
    # Create shortcuts
    CreateDirectory "$SMPROGRAMS\${APP_NAME}"
    CreateShortCut "$SMPROGRAMS\${APP_NAME}\Uninstall.lnk" "$INSTDIR\${APP_UNINSTALL}" "" "$INSTDIR\${APP_UNINSTALL}" 0
    CreateShortCut "$SMPROGRAMS\${APP_NAME}\${APP_NAME}.lnk" "javaw.exe" "-jar $\"$INSTDIR\${APP_MAIN}$\"" "$INSTDIR\application.ico" 0
    CreateShortCut "$SMPROGRAMS\${APP_NAME}\${APP_NAME} [Console].lnk" "java.exe" "-jar $\"$INSTDIR\${APP_MAIN}$\"" "java.exe" 0
    CreateShortCut "$SMPROGRAMS\${APP_NAME}\Samples.lnk" "$INSTDIR\samples" "" "$INSTDIR\samples"

    CreateShortCut "$DESKTOP\${APP_NAME}.lnk" "javaw.exe" "-jar $\"$INSTDIR\${APP_MAIN}$\"" "$INSTDIR\application.ico" 0
SectionEnd

# --------------------------------
# Descriptions

# Language strings
LangString DESC_SecDummy ${LANG_ENGLISH} "${APP_NAME} application files"
LangString DESC_SecDummy ${LANG_GERMAN} "${APP_NAME} Programm Dateien"

# Assign language strings to sections
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"
  # ADD YOUR OWN FILES HERE...
  
  # Remove shortcuts, if any
  Delete "$SMPROGRAMS\${APP_NAME}\*.*"

  # Remove directories used
  RMDir "$SMPROGRAMS\${APP_NAME}"

  Delete "$INSTDIR\${APP_UNINSTALL}"

  RMDir /r "$INSTDIR"

  DeleteRegKey /ifempty HKLM "${APP_REGKEY}"

SectionEnd

Function DonatePage
	!insertmacro MUI_HEADER_TEXT "Installation finshed" "The installation is successfull finished."

	nsDialogs::Create 1018
	Pop $hwnd
	SetCtlColors $hwnd 0xFFFFFF 0x6D6D6D
	
	${If} $hwnd == error
		Abort
	${EndIf}
	
	${NSD_CreateLabel} 0 0 100% 40 "Please support this project by a donation. Any donations to this project will help support continued maintenance and development of HL7 Inspector"
	Pop $R0
	SetCtlColors $R0 0xFFFFFF 0x6D6D6D
	
	${NSD_CreateButton} 0 60 40 12u "Donate"
	Pop $hwnd
	${NSD_OnClick} $hwnd CallDonateURL
	
	#${NSD_CreateCheckbox} 0 160 95% 12u "Run HL7 Inspector"
	#Pop $Hwd_RunApp
	#SetCtlColors $Hwd_RunApp 0xFFFFFF 0x6D6D6D
 
	nsDialogs::Show
FunctionEnd

Function DonatePageLeave
#	${NSD_GetState} $Hwd_RunApp $0
#	${If} $0 == 1
#		ExecShell "open" "${MUI_FINISHPAGE_RUN} ${MUI_FINISHPAGE_RUN_PARAMETERS}"
#	${EndIf}
FunctionEnd

Function CallDonateURL
	ExecShell "open" "http://sourceforge.net/donate/index.php?group_id=158007"
FunctionEnd


Function .onInit
    # TODO Check installed jre/jdk
    # MessageBox MB_YESNO "Java Runtime not found. Continue?" IDYES NoAbort
    # Abort;
    # NoAbort:
FunctionEnd