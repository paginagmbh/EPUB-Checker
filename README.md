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


License
-------

Currently licensed under a [CreativeCommons Attribution-NoDerivs 3.0](http://creativecommons.org/licenses/by-nd/3.0/) license (the binary distributables), once it's released open-source, the `de.paginagmbh.*` java sources and the build toolchain are licensed under a [GNU General Public License v2.0](http://choosealicense.com/licenses/gpl-2.0/) license unless the code comments specify the contrary.

We use the following libraries to build our GUI wrapper around *epubcheck*:
* [epubcheck](https://github.com/IDPF/epubcheck) 4.0.1
  * *New BSD License*
* [JSON RPC](http://mvnrepository.com/artifact/com.metaparadigm/json-rpc/1.0) 1.0
  * *Apache License 2.0*
* [Apple Java Extensions](http://mvnrepository.com/artifact/com.apple/AppleJavaExtensions/1.4) 1.4
  * *Apple License*

To build the EPUB-Checker app, we use the following tools and libraries:
* [Jarbundler v2.4](https://github.com/tofi86/Jarbundler) *(a forked version of [Jarbundler](http://informagen.com/JarBundler/))*
  * *Apache License v2.0*
* [launch4j](http://launch4j.sourceforge.net/)
  * *BSD license / MIT License*
* [universalJavaApplicationStub](https://github.com/tofi86/universalJavaApplicationStub)
  * *MIT License*


Disclaimer
----------

This is no high-level Java application! :smirk:

We started this project as a test case for cross platform Java development but released binaries to the public very soon. We learned a lot about generating native apps from Java (like Windows `.exe` and Apple `.app` files) or writing a update mechanism and you're always welcome to ask questions relating to this.

However, we can't guarantee that our code is free of bugs.


ANT-Task
----------------

The ANT build.xml needs the following libraries:

* [jarbundler 2.4.0](https://github.com/tofi86/Jarbundler/releases/tag/v2.4.0 "Jarbundler 2.4.0 Download")
* [launch4j 3.x](https://sourceforge.net/projects/launch4j/files/launch4j-3/ "launch4j 3.x Download")

which need to be placed here:

* git root
  * `bin/`
  * `buildConfig/`
    * `lib/`
      * `jarbundler/`
        * `jarbundler.jar`
        * *plus dependencies...*
      * `launch4j/`
        * `launch4j.jar`
        * *plus dependencies...*
  * `dist/`
  * `src/`

If these two libraries are in the correct place, it should be easy to build the EPUB-Checker project from sources!

**Note:** The `build.xml` file contains a Mac `codesign` task to sign and verify the Mac App. Therefore, this part will only work on a Mac. The codesign task also references our Apple Developer Certificate which we use to distribute the binary files [on our website](http://www.pagina-online.de/produkte/epub-checker/). It's probably better for you to remove the codesign tasks in order to be able to build the distributables...
