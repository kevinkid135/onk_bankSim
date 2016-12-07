# SimBank

CISC327 Software Quality Assurance - Course Project

Team Onk:
* Kevin Chan (@kevinkid135)
* Niki Lin (@1nl6)
* Simon Zhang (@skhzhang)

Course project based on banking software not intended for actual use for anyone, but for practicing the building quality software using eXtreme Programming.

The software is built in Java with scripting done using bash.

For more details, see http://research.cs.queensu.ca/home/cordy/cisc327/index.html

# Setup

The following was successful using java version 1.7.0_111 on Bash. 

To create a .jar executable (and run it):

## Front End

Before we begin, make sure that the frontEnd/ directory are in the current directory (`ls` returns the 'frontEnd' folder).

1. Create the **.class files** from the .java files (.java -> .class)
  ```bash
  javac frontEnd/*.java
  ```
  This will compile the .java files.

1. Create the **.jar executable file** from the .class files (.class -> .jar)
  ```bash
  jar cfe SimBank-front.jar frontEnd.Onk frontEnd/*.class
  ```
  This will create a file 'SimBank-front.jar' where 'frontEnd.Onk' is the main class using the .class files in the 'frontEnd'   directory.

1. Execute the **.jar executable file**

  Make sure you are in a directory with the accounts list file and the transaction summary file.
  ```bash
  java -jar SimBank-front.jar accountList.txt tranSum.txt
  ```
  where '*accountList.txt*' is the accounts list file,
  and '*tranSum.txt*' is the transaction summary file

## Back Office

Run the same commands as above, but for the backEnd/ files.

1. Create the **.class files**
  ```bash
  javac backEnd/*.java
  ```
  
1. Create the **.jar executable** file
  ```bash
  jar cfe SimBank-back.jar backEnd.BackEnd backEnd/*.class
  ```
  
1. Execute the **.jar executable** file

  Make sure you are in a directory with the merged transaction summary file, the master accounts list file, and the valid accounts list file. Note that the changes to master accounts list will be written directly to file (file will be overwritten!).
  ```bash
  java -jar SimBank-back.jar mergedTranSum.txt masterAccList.txt validAccList.txt
  ```
  where '*mergedTranSum.txt*' is the merged transaction summary file,
  '*masterAccList.txt*' is the master accounts list file,
  and '*validAccList.txt*' is the valid accounts list file
