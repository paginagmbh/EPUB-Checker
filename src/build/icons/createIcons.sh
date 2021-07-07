#!/bin/bash

# Author: Tobias Fischer
# 
# Needs to be run on macOS which provides 'sips' and 'iconutil' tools.
# 'imageoptim' cli and 'imagemagick' must be installed manually
# (e.g. with brew install imageoptim-cli imagemagick)
# 
# Idea: http://stackoverflow.com/a/20703594/1128689


PNGsource="paginaEPUBChecker_1024"


# paginaEPUBChecker_128.icns for DMG sidebar icon
ICNSname="paginaEPUBChecker_128"
mkdir ${ICNSname}.iconset

sips -z 16 16     ${PNGsource}.png --out ${ICNSname}.iconset/icon_16x16.png
sips -z 32 32     ${PNGsource}.png --out ${ICNSname}.iconset/icon_16x16@2x.png
sips -z 32 32     ${PNGsource}.png --out ${ICNSname}.iconset/icon_32x32.png
sips -z 64 64     ${PNGsource}.png --out ${ICNSname}.iconset/icon_32x32@2x.png
sips -z 128 128   ${PNGsource}.png --out ${ICNSname}.iconset/icon_128x128.png
sips -z 256 256   ${PNGsource}.png --out ${ICNSname}.iconset/icon_128x128@2x.png

imageoptim "${ICNSname}.iconset/*.png"

iconutil -c icns ${ICNSname}.iconset


# paginaEPUBChecker_1024.icns for App icon
cp -r ${ICNSname}.iconset ${PNGsource}.iconset
rm -R ${ICNSname}.iconset
ICNSname="paginaEPUBChecker_1024"

sips -z 256 256   ${PNGsource}.png --out ${ICNSname}.iconset/icon_256x256.png
sips -z 512 512   ${PNGsource}.png --out ${ICNSname}.iconset/icon_256x256@2x.png
sips -z 512 512   ${PNGsource}.png --out ${ICNSname}.iconset/icon_512x512.png
cp ${PNGsource}.png ${ICNSname}.iconset/icon_512x512@2x.png

imageoptim "${ICNSname}.iconset/*.png"

iconutil -c icns ${ICNSname}.iconset


# paginaEPUBChecker_512.ico for Windows
ICOname="paginaEPUBChecker_512"
sips -z 48 48   ${PNGsource}.png --out ${ICNSname}.iconset/icon_48x48.png
convert \
    ${ICNSname}.iconset/icon_16x16.png \
    ${ICNSname}.iconset/icon_32x32.png \
    ${ICNSname}.iconset/icon_48x48.png \
    ${ICNSname}.iconset/icon_128x128.png \
    ${ICNSname}.iconset/icon_256x256.png \
    ${ICNSname}.iconset/icon_512x512.png \
    ${ICOname}.ico


# provide updated 'resources' icons
target_resources="../../main/resources/icons"
cp ${ICNSname}.iconset/icon_16x16.png ${target_resources}/paginaEPUBChecker_16.png
cp ${ICNSname}.iconset/icon_32x32.png ${target_resources}/paginaEPUBChecker_32.png
cp ${ICNSname}.iconset/icon_32x32@2x.png ${target_resources}/paginaEPUBChecker_64.png
cp ${ICNSname}.iconset/icon_128x128.png ${target_resources}/paginaEPUBChecker_64@2x.png
cp ${ICNSname}.iconset/icon_128x128.png ${target_resources}/paginaEPUBChecker_128.png
cp ${ICNSname}.iconset/icon_256x256.png ${target_resources}/paginaEPUBChecker_256.png
cp ${ICNSname}.iconset/icon_512x512.png ${target_resources}/paginaEPUBChecker_512.png
cp ${PNGsource}.png ${target_resources}/paginaEPUBChecker_1024.png

rm -R ${ICNSname}.iconset
