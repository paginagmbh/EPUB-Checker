#!/bin/bash

# http://stackoverflow.com/a/20703594/1128689

ICNSname="paginaEPUBChecker_1024"

mkdir ${ICNSname}.iconset

sips -z 16 16     ${ICNSname}.png --out ${ICNSname}.iconset/icon_16x16.png
sips -z 32 32     ${ICNSname}.png --out ${ICNSname}.iconset/icon_16x16@2x.png
sips -z 32 32     ${ICNSname}.png --out ${ICNSname}.iconset/icon_32x32.png
sips -z 64 64     ${ICNSname}.png --out ${ICNSname}.iconset/icon_32x32@2x.png
sips -z 128 128   ${ICNSname}.png --out ${ICNSname}.iconset/icon_128x128.png
sips -z 256 256   ${ICNSname}.png --out ${ICNSname}.iconset/icon_128x128@2x.png
sips -z 256 256   ${ICNSname}.png --out ${ICNSname}.iconset/icon_256x256.png
sips -z 512 512   ${ICNSname}.png --out ${ICNSname}.iconset/icon_256x256@2x.png
sips -z 512 512   ${ICNSname}.png --out ${ICNSname}.iconset/icon_512x512.png

cp ${ICNSname}.png ${ICNSname}.iconset/icon_512x512@2x.png

iconutil -c icns ${ICNSname}.iconset

rm -R ${ICNSname}.iconset
