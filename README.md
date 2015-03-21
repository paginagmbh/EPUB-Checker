pagina EPUB-Checker
============

Standalone "EPUB-Checker" application for Windows, Mac OS X and Linux


ANT-Task
--------

The ANT build.xml needs the following libraries:

* [jarbundler 2.3.0](https://github.com/tofi86/Jarbundler/releases/tag/v2.3.0 "Jarbundler 2.3.0 Download")
* [launch4j 3.x](https://sourceforge.net/projects/launch4j/files/launch4j-3/ "launch4j 3.x Download")
 * _Für Mac OS X Mountain Lion steht ein extra Build (3.1 beta) zur Verfügung_

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