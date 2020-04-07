pagina EPUB-Checker
===================

![Java CI with Maven](https://github.com/paginagmbh/EPUB-Checker/workflows/Java%20CI%20with%20Maven/badge.svg)

Standalone "EPUBCheck" application for Windows, macOS and Linux.

With the pagina EPUB-Checker one can easily validate eBooks in the EPUB format. The test mechanisms of the EPUB-Checker are based on the official open-source [EPUBCheck](https://github.com/w3c/epubcheck) EPUB validator.


Features
--------

pagina EPUB-Checker wraps up this tool and offers some additional features like:

* Graphical user interface
* Drag & drop EPUB to validate
* Ability to validate expanded (unzipped) EPUB's
  * Expanded folders are automatically zipped up to an EPUB file upon validation
  * The generated EPUB file will be saved if it is valid
* Localized GUI and errors and warnings:
  * English
  * German
  * French
  * Spanish
  * Japanese
  * Portuguese (Brazil)
  * Russian (messages incomplete)
  * Dutch (messages only, english GUI)
  * Czech (GUI only, english messages)
  * Traditional Chinese (Taiwan) (GUI only, english messages)
  * Turkish (GUI only, english messages)
  * Danish

pagina EPUB-Checker doesn't need to be installed and therefore works on portable USB devices as well as on computers with restricted rights.


Download
--------

Please visit our website https://www.pagina.gmbh/produkte/epub-checker/ to download the Windows _EXE_ file, the Mac _App_ or the Linux _JAR_.

This is just the source code repository. You won't find any pre-build binaries here...


License information
-------------------

Our app and code and all the java sources in `de.paginagmbh.*` are licensed under the terms of the  [GNU General Public License v2.0](http://choosealicense.com/licenses/gpl-2.0/) unless the code comments specify the contrary.

We use the following Java libraries to build our GUI wrapper around *EPUBCheck*:
* [EPUBCheck](https://github.com/w3c/epubcheck) 4.2.2 (*3-Clause BSD License*)
* [JSON RPC](http://mvnrepository.com/artifact/com.metaparadigm/json-rpc/1.0) 1.0 (*Apache License 2.0*)
* [Apple Java Extensions](http://mvnrepository.com/artifact/com.apple/AppleJavaExtensions/1.4) 1.4 (*Apple License*)

To build the EPUB-Checker app, we use the following tools and libraries (among other Maven tools):
* [Jarbundler](https://github.com/UltraMixer/JarBundler) (*Apache License v2.0*)
* [launch4j](http://launch4j.sourceforge.net/) (*BSD license / MIT License*)
* [universalJavaApplicationStub](https://github.com/tofi86/universalJavaApplicationStub) (*MIT License*)
* [electron-installer-dmg](https://github.com/electron-userland/electron-installer-dmg) (*Apache License v2.0*)
* [gon](https://github.com/mitchellh/gon) (*MIT License*)


Build the app
-------------

In order to build the Linux JAR, the Mac App and the Windows EXE files you just have to run

```
mvn clean package
```

from the root directory of this project.

This will build the executables but skip the macOS specific codesigning process by default.


### Build requirements

* Java/JDK 8+
* Maven 3.5+


Release the app
---------------

Releasing a new version requires the Mac App to be codesigned and notarized. This can be done from the maven packaging process or via GitHub Actions CI on the `master` branch. The additional maven step will run a bash script (`src/build/mac-release.sh`) to codesign and notarize the Mac App with our private Apple Developer Certificate. Therefore, this step will only work on our systems or in GitHub Actions CI.

### Release requirements

* macOS 10.14+
* Java/JDK 8
* Maven 3.5+
* NodeJS + npm
* Homebrew

*App codesigning*

Codesigning is done with the default macOS `codesign` utility

*App notarization*

App notarization is done with [gon](https://github.com/mitchellh/gon), an excellent utility for this job. It will be installed via HomeBrew if it's missing.

To be able to submit the App for notarization, you need to copy `src/build/gon-dmg-config.template.json` to `src/build/gon-dmg-config.json` and fill the `apple_id` credentials.

*DiskImage creation*

DiskImage creation is done with the NodeJS utility [electron-installer-dmg](https://github.com/electron-userland/electron-installer-dmg). It will be installed via NPM if it's missing.

### Build the release locally

To build the JAR's, the Windows EXE and Mac App and to run the Mac App codesigning and notarization process for distribution _locally,_ you have to enable the _skipped-by-default_ maven task with:

```
mvn -Dmaven.skip.macSigning=false clean package
```


### Build & release with GitHub Actions

To build and release with GitHub Actions CI, just merge a _snapshot version_ from `development` to `master`. No need to upgrade the Maven version first or to set a git tag. Just merge to `master` and CI is doing all the hard work (as defined in `.github/workflows/release.yml`).

The release distributables are attached to the GitHub Actions build as build artifacts and can be used for distribution on our download server.
