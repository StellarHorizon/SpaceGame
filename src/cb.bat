@echo off
echo Beginning Clean...
if not exist *.class goto process_already_clean
	echo Removing .class files...
	del *.class
	echo .class files removed.
	echo.
	goto process_clean_complete
:process_already_clean
	echo Already Clean.
	echo.
	goto process_clean_complete
:process_clean_complete
	echo Clean Process Complete.
	echo.
:process_start_build
	echo Starting build...
	javac *.java
	echo Build Complete!
	echo.
	goto process_script_complete
:process_script_complete
	echo.
	echo Clean-Build Complete.