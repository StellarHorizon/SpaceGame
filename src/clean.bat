@echo off
echo Beginning Clean...
if not exist *.class goto process_already_clean
	echo Removing .class files...
	del *.class
	echo .class files removed.
	goto process_clean_complete
:process_already_clean
	echo Already Clean.
	goto process_clean_complete
:process_clean_complete
	echo Clean Process Complete.