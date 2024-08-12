# Guide For Building and Deploying Parking Rates REST API

The BuildAndDeploy.bat file located within the pkrates-web-api folder will configure and make the Parking Rates API web application. Little or no intervention is required by the person running this BuildAndDeploy.bat file. It requires an installation to exist on the machine that is running it in order to work. This is listed below.

## GO
This API is written in Go and requires a version of Go installed on your machine. It should be at least version 1.22.5. To aquire Go, install, and configure please refer to https://go.dev/doc/install

## Build Steps
1. Once you have completed the above installation instructions locate and execute the BuildAndDeploy.bat file located in the build folder.
2. Server should be running on http://localhost:8080/ and ready to accept requests.

### To manually stop server
Close command window that opened when executing step 1 above.