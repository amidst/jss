#!/bin/bash
#


JAVA_VER=$(java -version 2>&1 | sed 's/java version "\(.*\)\.\(.*\)\..*"/\1\2/; 1q');


if [ "$JAVA_VER" -lt 18 ];
then
	echo "ERROR: wrong JAVA version, AMIDST requires version 1.8 or higher";
	exit;
fi


javac -sourcepath src -d bin -cp lib/amidst-0.6.1.jar src/main/java/sect31datastreams.java;
java -cp .:bin:lib/amidst-0.6.1.jar sect31datastreams;