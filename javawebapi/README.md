# Guide For Building and Deploying Parking Rates REST API

The build.xml file located within the **build** folder will configure and make the Parking Rates API web application. Little or no intervention is required by the person running this build.xml file. It requires a few things installed on the machine that is running it in order to work. These are listed below.

## ANT
Not suprisingly as the build.xml file is written in ANT, it does require that a version of ANT is installed on your machine. It should be version 1.9.4 or greater. To aquire ANT, install, and configure please refer to https://ant.apache.org/

### ant-contrib
ant-contrib is a set of extensions to ANT that provide some decision style of control not included in the basic ANT distribution. These are things like if and for loops. You can obtain it at https://ant-contrib.sourceforge.net. You only need to add the jar file (ant-contrib-XXX.jar) to the lib directory underneath your ANT installation. So install ANT first.

## Java
In addition to whatever version of the Java runtime environment you may have (and must have to make ANT work) on your system you will need a JDK (Java Development Kit) installed on your system so that the *.java files can be compiled. Currently this must be version 1.8. Install this as you normally would and then MAKE SURE that your 'JAVA_HOME' environment variable is set correctly, pointing to the jdk1.8 installation. Also MAKE SURE that you have the value %JAVA_HOME%\bin inside of your 'Path' environment variable value list. If you do not have these environment variables you must create them and set their values. A shortcut to modify these values is: 

1. WINDOWS KEY + Pause/Break (keyboard shortcut)
2. Advanced System Properties (link or tab within window)
3. Environment Variables... (button)

An example of the environment variables explained is below:

### EXAMPLES
- Variable name:  JAVA_HOME
- Variable value:  C:\Program Files\Java\jdk-1.8

You can get the Development Kit at https://www.oracle.com/java/technologies 

## Build Steps
1. Once you have completed the above installation instructions locate and open the build.properties file located in the build folder. Locate the property named **COPY.directory** and make sure the value which is a directory path exists on your machine. If it does not please create the directory because this is where the initial Rates.json file will be copied to during the build process. The Rates.json file contains the list of rates that will be loaded on server startup.
2. Execute the build process by opening a command prompt within the build directory and execute the ant command--this will start the building.
3. Wait for the **BUILD SUCCESSFUL** message from the build script.
4. Server should be running on http://localhost:8080/ and ready to accept requests.

## To manually stop server
1. Open the deploy folder located within the build directory
2. Execute the stopServer.bat file

## To manually start server
1. Open the deploy folder located within the build directory
2. Execute the startServer.bat file

## NOTE
Application logging has been implemented and has been defaulted to the following location on your machine: c:/temp/pkrates