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
!define APP_REGKEY "Software\${APP_MANUFACTOR}\${APP_NAME}" 
!define APP_UNINSTALL "Uninstall ${APP_MANUFACTOR} ${APP_NAME}.exe"
!define APP_MAIN "hl7inspector.exe"

!include "elomagicLF.nsh"

# Name and file
Name "${APP_MANUFACTOR} ${APP_NAME} ${APP_VERSION}"
OutFile ".\dist\${APP_MANUFACTOR} ${APP_NAME} ${APP_VERSION} Setup.exe"

# Default installation folder
InstallDir "$PROGRAMFILES\${APP_MANUFACTOR}\${APP_NAME}"
  
# Get installation folder from registry if available
InstallDirRegKey HKLM "${APP_REGKEY}" ""

# Request application privileges for Windows Vista
RequestExecutionLevel admin

# --------------------------------
# Interface Settings

!define MUI_ABORTWARNING
!define MUI_FINISHPAGE_RUN "${APP_MAIN}"

;--------------------------------
;Pages
  !insertmacro MUI_PAGE_LICENSE "License.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_PAGE_FINISH
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"
  !insertmacro MUI_LANGUAGE "German"

;--------------------------------
;Installer Sections

Section "${APP_NAME} ${APP_VERSION}" SecDummy

  SetOutPath "$INSTDIR"
  
  ;ADD YOUR OWN FILES HERE...
  File /r ".\..\dist\"
  
  ;Store installation folder
  WriteRegStr HKLM "${APP_REGKEY}" "" $INSTDIR 

  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "DisplayName" "${APP_MANUFACTOR} ${APP_NAME}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "UninstallString" '"$INSTDIR\${APP_UNINSTALL}"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_MANUFACTOR}${APP_NAME}" "NoRepair" 1
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\${APP_UNINSTALL}"
  
  # Create shortcuts
  CreateDirectory "$SMPROGRAMS\${APP_NAME}"
  CreateShortCut "$SMPROGRAMS\${APP_NAME}\Uninstall.lnk" "$INSTDIR\${APP_UNINSTALL}" "" "$INSTDIR\${APP_UNINSTALL}" 0
  CreateShortCut "$SMPROGRAMS\${APP_NAME}\${APP_NAME}.lnk" "javaw.exe" "-jar $INSTDIR\certtool-standalone.jar" "$INSTDIR\${APP_UNINSTALL}" 0  

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