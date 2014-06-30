pagina EPUB-Checker
============

Standalone "EPUB-Checker" application for Windows, Mac OS X and Linux


ANT-Task
--------

Der ANT-Build-Task benötigt folgende Libraries:

* [jarbundler 2.3.0](https://github.com/tofi86/Jarbundler/releases/tag/v2.3.0 "Jarbundler 2.3.0 Download")
* [launch4j 3.x](https://sourceforge.net/projects/launch4j/files/launch4j-3/ "launch4j 3.x Download")
 * _Für Mac OS X Mountain Lion steht ein extra Build (3.1 beta) zur Verfügung_
* [xmltask](https://sourceforge.net/projects/xmltask/files/xmltask/)
 * _Nur für die ANT-Skripte zum automatischen veröffentlichen. Wird aber momentan nicht genutzt!_

und folgenden Ordneraufbau:

* EPUB-Checker (java sources)
* _ANT-buildDir
	* libs/
		* jarbundler-2.2.0/
			* jarbundler-2.3.0.jar
		* launch4j/  _(!! Mac/Windows spezifisch !!)_
			* launch4j.jar
		* xmltask.jar
	* resources/
		* JavaApplicationStub
	* output/

Unter diesen Voraussetzungen kann der ANT-Task aus Eclipse heraus gestartet werden.
