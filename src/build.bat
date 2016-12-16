@echo off
if exist *.class goto process_was_unclean
rem if no class variables found, start build immediately
goto :process_start_build

:process_was_unclean
set /p uncleanDirContinue="WARNING: .class files already present in directory. Continue (Y/N)? "
if /i "%uncleanDirContinue%" == "Y" goto :process_continue_build
if /i "%uncleanDirContinue%" == "Y" goto :process_abort_build
else goto :process_invalid_input

:process_continue_build
echo Continuing...
goto :process_start_build
:process_abort_build
echo Build Aborted!
goto :EOF
:process_invalid_input
echo Please enter a valid input.
goto :process_was_unclean

:process_start_build
echo Build process started...
javac *.java
echo Build Complete!