#!/bin/bash

# author: Tobias Fischer
# copyright: pagina GmbH, Tübingen
# date: 2020-03-22
#
# general tips about notarization
# - http://www.zarkonnen.com/signing_notarizing_catalina
#
# This script requires the following environment variables
# - MVN_BASEDIR    the maven base dir
# - MVN_BUILDDIR   the maven build dir (target)
# - APP_NAME       the app name
# - APP_NAME_LONG  the long app name
# - APP_VERSION    the app version
# - APPLE_SIGN_ID  the apple developer signing identity


# enable 'fail-fast' to exit the build script immediately on failures
set -e


# build file paths
APP_PATH="${MVN_BUILDDIR}/${APP_NAME}.app"
DMG_PATH="${MVN_BUILDDIR}/${APP_NAME}.dmg"


# update App Info.plist with an additional key
/usr/libexec/PlistBuddy -c "Add :NSAppleEventsUsageDescription string There was an error while launching ${APP_NAME_LONG}. Please click OK to display a dialog with more information or cancel and view the syslog for details." \
  "${APP_PATH}/Contents/Info.plist"

# try to add file extension to license files
echo "trying to add file extension to  license files"
find . -name "LICENSE" -exec bash -c 'echo mv $0 ${0/LICENSE/LICENSE.txt}' {} \;
find /tmp -name "LICENSE" -exec bash -c 'echo mv $0 ${0/LICENSE/LICENSE.txt}' {} \;
find /Users/runner/work/EPUB-Checker/ -name "LICENSE" -exec bash -c 'echo mv $0 ${0/LICENSE/LICENSE.txt}' {} \;
find "${MVN_BUILDDIR}" -name "LICENSE" -exec bash -c 'echo mv $0 ${0/LICENSE/LICENSE.txt}' {} \;
find "${MVN_BASEDIR}" -name "LICENSE" -exec bash -c 'echo mv $0 ${0/LICENSE/LICENSE.txt}' {} \;
find "${APP_PATH}" -name "LICENSE" -exec bash -c 'echo mv $0 ${0/LICENSE/LICENSE.txt}' {} \;
find "${DMG_PATH}" -name "LICENSE" -exec bash -c 'echo mv $0 ${0/LICENSE/LICENSE.txt}' {} \;


# codesign the Mac App
/usr/bin/codesign --force --verbose --options runtime --sign "${APPLE_SIGN_ID}" "${APP_PATH}"
# verify the signature
/usr/bin/codesign --verify --strict --deep --verbose "${APP_PATH}"
spctl -a -t exec -vv "${APP_PATH}"


# Create DMG with Node package 'electron-installer-dmg'
which electron-installer-dmg || ( npm install -g electron-installer-dmg )
electron-installer-dmg \
  --title="${APP_NAME_LONG} ${APP_VERSION}" \
  --out="${MVN_BUILDDIR}" \
  --icon=src/build/icons/paginaEPUBChecker_128.icns \
  --background=src/build/splashscreen/DmgBackground.png \
  --overwrite \
  "${APP_PATH}" \
  "${APP_NAME}"

# codesign the DiskImage
/usr/bin/codesign --force --verbose --options runtime --sign "${APPLE_SIGN_ID}" "${DMG_PATH}"
# verify the signature
/usr/bin/codesign --verify --strict --deep --verbose "${DMG_PATH}"


# only notarize the signed disk image which contains the signed app bundle
#
# from apple docs:
# > For example, if you submit a disk image that contains a signed installer
# > package with an app bundle inside, the notarization service generates tickets
# > for the disk image, installer package, and app bundle.
# > https://developer.apple.com/documentation/xcode/notarizing_macos_software_before_distribution/customizing_the_notarization_workflow#3087734

# notarize the Mac App with 'gon' (https://github.com/mitchellh/gon)
which gon || ( brew tap mitchellh/gon && brew install mitchellh/gon/gon )
if ! gon -log-level=info -log-json "${MVN_BASEDIR}/src/build/gon-dmg-config.json"; then
     echo "command exited with non-zero exit code"
fi

# validate the notarization process of the dmg
/usr/bin/xcrun stapler validate "${DMG_PATH}"
/usr/sbin/spctl -a -t install -vv "${DMG_PATH}"

# staple the notarization ticket to the App which may also be released in a ZIP file
/usr/bin/xcrun stapler staple "${APP_PATH}"
/usr/bin/xcrun stapler validate "${APP_PATH}"
