pagina EPUB-Checker [![Build Status](https://travis-ci.org/paginagmbh/EPUB-Checker.svg?branch=master)](https://travis-ci.org/paginagmbh/EPUB-Checker)
===================

Standalone "EPUB-Checker" application for Windows, Mac OS X and Linux.

With the pagina EPUB-Checker one can easily validate eBooks in the EPUB format. The test mechanisms of the EPUB-Checker are based on the official open-source [EPUBCheck tool](https://github.com/w3c/epubcheck) (version 4.2.2).

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

Please visit our website https://www.pagina.gmbh/produkte/epub-checker/ to download the Windows _EXE_ file, the Mac OS _App_ or the Linux _JAR_.

This is just the source code repository. You won't find any binary downloads here...


License information
-------------------

Our app and code and all the java sources in `de.paginagmbh.*` are licensed under the terms of the  [GNU General Public License v2.0](http://choosealicense.com/licenses/gpl-2.0/) unless the code comments specify the contrary.

We use the following libraries to build our GUI wrapper around *EPUBCheck*:
* [EPUBCheck](https://github.com/w3c/epubcheck) 4.2.2
  * *3-Clause BSD License*
* [JSON RPC](http://mvnrepository.com/artifact/com.metaparadigm/json-rpc/1.0) 1.0
  * *Apache License 2.0*
* [Apple Java Extensions](http://mvnrepository.com/artifact/com.apple/AppleJavaExtensions/1.4) 1.4
  * *Apple License*

To build the EPUB-Checker app, we use the following tools and libraries (among other maven tools):
* [Jarbundler](https://github.com/UltraMixer/JarBundler)
  * *Apache License v2.0*
* [launch4j](http://launch4j.sourceforge.net/)
  * *BSD license / MIT License*
* [universalJavaApplicationStub](https://github.com/tofi86/universalJavaApplicationStub)
  * *MIT License*


Build the app
-------------

In order to build the Linux JAR, the Mac App and the Windows EXE files you just have to run

```
mvn -Dmaven.codesign.skip=true clean package
```

from the root directory of this project.

This will build the executables but skip the Mac OS specific codesigning process.


### Important Note


Release the app
---------------

The maven packaging process can run a Mac OS specific release task to sign and notarize the Mac App with our (private) Apple Developer Certificate. Therefore, this step will only work on our systems.

Releasing a new version requires the Mac OS App to be codesigned and notarized. This is done with a specific maven exec call to the `gon` command, an excellent wrapper for this job. Make sure you have [gon](https://github.com/mitchellh/gon) installed.

To be able to build signed packages on other Mac OS systems, you need to copy `src/build/gon-config.template.json` to `src/build/gon-config.json` and fill all empty keys.

Then you can run the following to build the app:

```
mvn clean package
```

Signing can be skipped with:

```
mvn -Dmaven.codesign.skip=true clean package
```

Signing during `mvn release:prepare` phase (with `maven-release-plugin`) can be skipped with:

```
mvn -Darguments=-Dmaven.codesign.skip=true clean release:prepare
```
