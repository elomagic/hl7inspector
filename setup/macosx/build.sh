#!/bin/bash
echo Build HL7 Inspector Setup
echo Prepare build process
rm 'dist/HL7Inspector Setup.pkg'
echo
echo ==================================
echo
echo Build Mac OS setup file
echo
/Developer/Applications/Utilities/PackageMaker.app/Contents/MacOS/PackageMaker -d 'HL7Inspector Setup.pmdoc' -v -o 'dist/HL7Inspector Setup.pkg'
echo
echo ==================================
echo
echo Build Mac OS setup file finished
