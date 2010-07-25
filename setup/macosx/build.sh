#!/bin/bash
echo Build HL7 Inspector Setup
echo
echo Preparing build process
rm -R dist/*
rm -R src/*
echo

echo ==================================
echo
echo Collecting setup files
mkdir -p "src/HL7 Inspector.app/Contents/MacOS"
mkdir -p "src/HL7 Inspector.app/Contents/Resources/Java"

# Copy system resource
cp /System/Library/Frameworks/JavaVM.framework/Versions/Current/Resources/MacOS/JavaApplicationStub "src/HL7 Inspector.app/Contents/MacOS/JavaApplicationStub"
chmod ugo+rx "src/HL7 Inspector.app/Contents/MacOS/JavaApplicationStub"

# Copy application icon
cp resources/AppIcon.icns "src/HL7 Inspector.app/Contents/Resources/AppIcon.icns"


# Copy package info files

# TODO Replace #APP_VERSION# with version text
# sed -f ~/.sedfile $file.old > $file
cp resources/Info.plist.xml "src/HL7 Inspector.app/Contents/Info.plist"

cp resources/PkgInfo "src/HL7 Inspector.app/Contents/PkgInfo"

# Copy main application
cp ../../dist/hl7inspector.jar "src/HL7 Inspector.app/Contents/Resources/Java/hl7inspector.jar"

# Copy libs
cp ../../dist/lib/*.* "src/HL7 Inspector.app/Contents/Resources/Java"
echo
echo ==================================
echo
echo Building Mac OS setup file
echo
/Developer/Applications/Utilities/PackageMaker.app/Contents/MacOS/PackageMaker -d 'HL7Inspector Setup.pmdoc' -v -o 'dist/HL7Inspector Setup.pkg'
echo
echo ==================================
echo
echo Build Mac OS setup file finished. Check the result!
