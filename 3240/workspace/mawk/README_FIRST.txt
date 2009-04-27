Micro-Awk Parser

Anthony Marion
Nicholas Marquez
Parker McGee


* To build:   
Requirements: Apache Ant (http://ant.apache.org/), Java JDK

On the command line navigate to the mawk directory and issue 'ant'. It will automatically detect the build.xml file and begin building.
 ->Be certain that Ant and your environment is configured properly - it must be able to find the java compiler (javac) specifically.


* To run:

Ant will build a jar file and copy a few example scripts and data files into the release/ directory. To run the jar file, cd into the release/ directory and issue the following on the command line:
	java -jar mawk.jar [args]
Usage will be printed out if no args are supplied (or invalid args).

You can also run some example scripts/data files with ant. To see all the supplied ant targets type:
	ant -p
at the command line in the mawk folder (that which contains build.xml). All targets listed with the word example are example targets (save copyExamples). To see what exactly is being passed, look in the build.xml file (it is mostly self-explanatory).


* Other notes:

Abstract Syntax Trees are printed in a directory-tree-like graphical structure, where child nodes are graphically delineated such that there is no ambiguity or difficult reading involved.

The structure of adjacency matrices are also easy to interpret, as they are tables identical to what is shown in the PDF file.   

Some adjacency matrix outputs may be too wide for the Windows command-line window (or non-expanded *nix terminals) and word-wrap, simply widen the respective windows.