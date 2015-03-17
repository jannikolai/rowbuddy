# Manual #

## Install eclipse subversion plugin ##
  1. Help -> Eclipse Marketplace
  1. Choose Eclipse Marketplace
  1. Search for subversion
  1. Install Subversive
  1. Restart Eclipse
  1. On startup you are asked to install a svn connector -> choose SVN Kit 1.3.3
  1. Restart Eclipse
  1. Checkout sources:
> a.Window -> Open Perspective -> Others -> SVN Repository Exploring

> b.Press the "Plus" icon -> Enter Repos data + authentication data

> c.Open repos -> Open Trunk -> select all ear, ejb and web project -> right click "checkout as"

> d. Select "check out following repository resources as projects"

> e.Finish
## Install Glassfish runtime plugin ##
    1. Help -> Eclipse Marketplace
    1. Choose Eclipse Marketplace
    1. Search for glassfish and install "GlassFish Java EE Aplication Server Plugin"
    1. Restart Eclipse
    1. Open server tab (Bottom of eclipse jee perspective)
    1. Right Click -> new Server
    1. Choose Glassfish 3
    1. Browse your glassfish install dir (e.g. /Application/glassfish-3.0.1/glassfish)
    1. Add the Ear project to the server

## Install GWT Plugin ##
  1. Help -> Eclipse Marketplace
  1. Choose Eclipse Marketplace
  1. Search for "gwt" and install "google Plugin for Eclipse"
  1. Restart Eclipse
## Install Derby Plugin ##
  1. Download plugin: http://db.apache.org/derby/derby_downloads.html -> Latest Version ->derby\_core\_plugin\_XXX  AND derby\_ui\_doc\_plugin\_XXX
  1. Extract the zip files into your ECLIPSE\_HOME/plugins folder
  1. Restart Eclipse

## Fix Projects ##
  1. Open Ejb project properties
  1. Click on Java Build Path -> Libraries and fix JRE system Library entry by edit this record
  1. Repeat 2. for the web project
  1. Click on Project -> Clean
  1. Run Ear File
> a. Start Derby DB by Right click on EJB Project -> Apache Derby -> Add Apache Derby nature instance (The plugin will now create a database folder within the ejb project Hint:Do not commit these files!)
> b. Right click on EJB Project -> Apache Derby -> Start Derby Network server
> c. Right click on ear project -> run on server -> choose glassfish and start

## SetUp development environment ##
  1. Enable Hot deploy: Server -> Right click on Glassfish -> Publishing -> Automatical publish when resources change
  1. Development mode for GWT: Right click on web project -> run on external Server -> Enter the following information:
|Server root|http://localhost:8080/Web_Project/|
|:----------|:---------------------------------|
|Matching items|welcomeGWT.html|

  1. Look for Development Tab and copy the url
  1. Paste the url into your firefox (plugin is required; don't worry you will be asked to install)
  1. If you receive some errors in your browser and google console: Right click on Web Project -> google -> GWT compile (This may required for first startup!)
  1. GWT hot deploy should work

## UnitTesting EJB Project ##
  1. Adjust the classpath variable to your Embedded Glassfish jar. This file is typically located under %glassfish\_home%\glassfish\lib\embedded\glassfish-embedded-static-shell.jar. The setting has to be made in the EJB project in Eclipse. Window->Preferences->Java->Build Path->Classpath Variables: EMBEDDED\_GLASSFISH.
  1. In order to run the tests click on a test (e.g. BoatTest) and choose RightClick->Run as->JUnit Test