README
======

pagina EPUB-Checker is a Java program provided to you as a 'runnable JAR' file.
This means, you can start the program by double-clicking the file
"paginaEPUBChecker.jar".

Please make sure you have a 'Java Runtime Environment' installed.
Either the 'Oracle JRE' or 'OpenJDK' packages.

If the JAR-File is being opened in Archiver program instead of showing the GUI,
please check the following:
1. make sure you installed a Java Runtime Environment
2. right click the JAR file and select "Properties"
	1. Open the tab "Permissions", make sure the checkbox at "Execute" is checked
	2. Open the tab "Open With" and make sure one of "OpenJDK Java 7/8 Runtime" or
		 "Oracle Java 8 Runtime" is selected
	3. Close the properties window and try again


Troubleshooting
---------------

If pagina EPUB-Checker finishes with errors but doesn't show them, try adjusting
the thread stack size of your Java Virtual Machine. With most Java
distributions, this can be done by using the -Xss option of the java command,
like in the following example:

java -Xss1024k -jar paginaEPUBChecker.jar

The downside of this method is, that you cannot run the JAR by double-clicking
any more. Consider writing a shell script wrapper in that case.
