; Install / Uninstall script for the elomagic HL7 Inspector
; Copyright 2007 
; Author: Carsten Rambow

;--------------------------------
;Include Modern UI

  !include "UMUI.nsh"

;--------------------------------
;General
  
  !define APPLICATION_VERSION "2.2.0.0"  
  !define APPLICATION_NAME "hl7inspector.exe"

  ;Name and file
  Name "elomagic HL7 Inspector"
  OutFile "dist\hl7incpector-${APPLICATION_VERSION}-setup.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\HL7 Inspector"

  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\elomagic\HL7 Inspector" ""

  ; Remove branding text
  BrandingText " "
	
  RequestExecutionLevel admin
  
  SetCompressor /SOLID lzma
  
  ; Icon of Setup
  !define MUI_ICON "application-setup.ico"
  !define MUI_UNICON "application-setup.ico"
  
;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
  !define MUI_FINISHPAGE_RUN $INSTDIR\${APPLICATION_NAME}
;--------------------------------
;Pages

;  !insertmacro MUI_PAGE_WELCOME
  ;!insertmacro MUI_PAGE_LICENSE $(MUILicense)
  !insertmacro MUI_PAGE_LICENSE "setup-dist\license\license-gpl.txt"
  ;!insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  !insertmacro MUI_UNPAGE_FINISH

;--------------------------------
;Languages

  ;first language is the default language
  !insertmacro MUI_LANGUAGE "English"
  ;!insertmacro MUI_LANGUAGE "German"

;--------------------------------
; Additional Setup Version Information

  VIProductVersion "${APPLICATION_VERSION}"
  
  VIAddVersionKey /LANG=${LANG_ENGLISH} "ProductName" "HL7 Inspector"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "Comments" ""
  VIAddVersionKey /LANG=${LANG_ENGLISH} "CompanyName" "elomagic"
;  VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalTrademarks" "Test Application is a trademark of Fake company"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "LegalCopyright" "Carsten Rambow"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "FileDescription" "elomagic HL7 Inspector"
  VIAddVersionKey /LANG=${LANG_ENGLISH} "FileVersion" "${APPLICATION_VERSION}"
  
;--------------------------------
;Installer Sections

Section "!HL7 Inspector" SecCore
  SectionIn 1 2 RO
  SetOutPath "$INSTDIR"

  ;ADD YOUR OWN FILES HERE...
  File /r /x ".svn" "setup-dist\*.*" 

  ;Store installation folder
  WriteRegStr HKCU "Software\elomagic\HL7 Inspector" "" $INSTDIR
    
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "DisplayName" "HL7 Inspector"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "UninstallString" "$INSTDIR\Uninstall.exe"  
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "DisplayIcon" "$INSTDIR\Uninstall.exe"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "DisplayVersion" "${APPLICATION_VERSION}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "Installation" "$INSTDIR"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "Publisher" "Carsten Rambow"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "URLUpdateInfo" "http://www.elomagic.de"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "URLInfoAbout" "http://www.elomagic.de"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "RegCompany" "elomagic"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "RegOwner" "Carsten Rambow"
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector" "NoRepair" 1

  ; Create shortcuts
  CreateDirectory "$SMPROGRAMS\HL7 Inspector"  
  CreateShortCut "$SMPROGRAMS\HL7 Inspector\HL7 Inspector.lnk" "$INSTDIR\${APPLICATION_NAME}" "" "$INSTDIR\${APPLICATION_NAME}"  
  CreateShortCut "$SMPROGRAMS\HL7 Inspector\Uninstall.lnk" "$INSTDIR\Uninstall.exe" "" "$INSTDIR\Uninstall.exe"  
  CreateShortCut "$SMPROGRAMS\HL7 Inspector\License.lnk" "$INSTDIR\license\license-gpl.txt"

  ; TODO Create context menu item
  
SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString ReqOS ${LANG_ENGLISH} "Microsoft Windows 2000 or higher is required.$\n$\nMicrosoft Windows NT/95/98/ME not supported."
  LangString ReqOS ${LANG_GERMAN} "Microsoft Windows 2000 oder höher wird benötigt.$\n$\nMicrosoft Windows NT/95/98/ME werden nicht unterstützt."  				
	
  LicenseLangString MUILicense ${LANG_ENGLISH} "setup-dist\license\license-gpl.txt"  
  ;LicenseLangString MUILicense ${LANG_GERMAN} "src\license_de.rtf"
	
  LangString DESC_SecShortCuts ${LANG_ENGLISH} "Creates shortcuts on the desktop and in the quick launch"
  LangString DESC_SecShortCuts ${LANG_GERMAN} "Erstellt Verknüpfungen auf dem Desktop und in der Schnellstartleiste"
  
  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecCore} "Recommend main files"
    !insertmacro MUI_DESCRIPTION_TEXT ${SecSamples} "Samples"
    !insertmacro MUI_DESCRIPTION_TEXT ${SecShortcuts} $(DESC_SecShortCuts)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  RMDir /r /REBOOTOK "$SMPROGRAMS\HL7 Inspector"
  
  RMDir /r /REBOOTOK "$INSTDIR"

  DeleteRegKey /ifempty HKCU "Software\elomagic\HL7 Inspector"
  
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\elomagicHL7Inspector"

SectionEnd

Function .onInit
;  GetVersion::WindowsVersion
;  Pop $R0
;  
;  IntCmpU $R0 5 done lessthan5 done
;  lessthan5:
;    MessageBox MB_ICONSTOP $(ReqOS)
;    Abort   
;  done:
	
	; TODO Check JRE Installation
FunctionEnd
