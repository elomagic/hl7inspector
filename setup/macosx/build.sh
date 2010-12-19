#!/bin/bash
AppVersion=`cat ../../target/classes/version.number`
echo Build HL7 Inspector $AppVersion Setup
echo
echo Preparing build process
rm -R dist/*
rmdir dist
rm -R src/*
rmdir src
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

# Replace #APP_VERSION# with $AppVersion
more resources/Info.plist.xml | sed "s/#APP_VERSION#/$AppVersion/g" > "src/HL7 Inspector.app/Contents/Info.plist"
# cp resources/Info.plist.xml "src/HL7 Inspector.app/Contents/Info.plist"

cp resources/PkgInfo "src/HL7 Inspector.app/Contents/PkgInfo"

# Copy main application
cp ../../dist/hl7inspector.jar "src/HL7 Inspector.app/Contents/Resources/Java/hl7inspector.jar"

# Copy libs
cp ../../dist/lib/*.* "src/HL7 Inspector.app/Contents/Resources/Java"
echo
echo ==================================
echo
echo Building Mac OS X HL7 Inspector $AppVersion Setup.pkg file
echo
SetupDist="dist/HL7 Inspector $AppVersion Setup.pkg"
mkdir -p "dist"
/Developer/Applications/Utilities/PackageMaker.app/Contents/MacOS/PackageMaker -d 'hl7inspector.pmdoc' -v -o "$SetupDist"
echo
echo ==================================
echo
echo Build Mac OS X HL7 Inspector $AppVersion Setup file finished. Check the result!
