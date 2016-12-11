pagina EPUB-Checker
============

Standalone "EPUB-Checker" application for Windows, Mac OS X and Linux

With the pagina EPUB-Checker one can easily validate eBooks in the EPUB format. The test mechanisms of the EPUB-Checker are based on the official open-source [epubcheck tools](https://github.com/IDPF/epubcheck) (version 4.0.1).

pagina EPUB-Checker capsules these tools and offers some additional features, like:

* Graphical user interface (like a "real" program)
* Drag & drop to validate
* Ability to validate expanded/unzipped EPUBs
  * Expanded folders are automatically archived to an EPUB file upon validation
  * The generated EPUB file will be saved if valid
* Localized GUI and errors and warnings:
  * English
  * German
  * French
  * Spanish
  * Russian (messages incomplete)
  * Japanese (messages only, english GUI)
  * Dutch (messages only, english GUI)

pagina EPUB-Checker doesn't need to be installed and therefore works on portable USB devices as well as on computers with restricted rights.


License information
-------------------

Our app and code and all the java sources in `de.paginagmbh.*` are licensed under the terms of the  [GNU General Public License v2.0](http://choosealicense.com/licenses/gpl-2.0/) unless the code comments specify the contrary.

We use the following libraries to build our GUI wrapper around *epubcheck*:
* [epubcheck](https://github.com/IDPF/epubcheck) 4.0.1
  * *New BSD License*
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
mvn clean package
```

from the root directory of this project.

### Important Note
The maven packaging process runs a Mac OS specific `codesign` task to sign and verify the Mac App with our (private) Apple Developer Certificate. Therefore, this part will only work on one of our Mac's.

To be able to build packages on other Mac systems or on Windows, just skip the codesigning task with the following option:

```
mvn -Dmaven.codesign.skip=true clean package
```

or during `release:prepare` phase (with `maven-release-plugin`):

```
mvn -Darguments=-Dmaven.codesign.skip=true clean package
```
