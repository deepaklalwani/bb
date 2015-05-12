*******************************************************************************
*                          Table of Contents                                  *
*******************************************************************************

 Section 1: Client Directory Information
 Section 2: About
 Section 3: Build Requirements
 Section 4: Eclipse Requirements
 Section 5: Webservice types POJO vs. Service
 Section 6: Installing webservices and Building blocks in a single process
 Section 7: Accessing webservices through Building blocks
 Section 8: Removing a webservice that is accessed through a Building
            block Factory
 Section 9: Resources Available on Edugarage

*******************************************************************************
*  Section 1:        Client Directory Information                             *
*******************************************************************************

===============================================================================
|Client Directory   | Description                                             |
===============================================================================
| client            | A java client jar                                       |
|                   | (Used by the javaclient and proxy server)               |
|                   | Start here first if you are a Java client developer.    |
-------------------------------------------------------------------------------
| dotnetclient      | A C# .net client library                                |
|                   | (Used by the qa/dotnet client application)              |
|                   | Start here first if you are a C# .net developer.        |
-------------------------------------------------------------------------------
| javaclient        | A java webservice client - for Java client developers   |
|                   | interested in building a Proxy Tool Without Placements. |
-------------------------------------------------------------------------------
| proxy/java        | A java Proxy Server - for Java client developers        |
|                   | interested in building a Proxy Tool With Placements.    |
-------------------------------------------------------------------------------
| qa/dotnet         | A C# .net webservice client application - for C# .net   |
|                   | developers interested in building a Proxy Tool Without  |
|                   | Placements.                                             |
-------------------------------------------------------------------------------
| sample_webservice | A sample webservice - for Java developers interested in |
|                   | creating their own Web Services.                        |
===============================================================================

*******************************************************************************
*  Section 2:                  About                                          *
*******************************************************************************

This readme file contains information about a sample webservice and is intended
for Java developers interested in creating their own web services. This sample 
webservice depends on the axis2 1.3 binary distribution and blackboard 
bb-platform.jar distribution.

*******************************************************************************
*  Section 3:             Build Requirements                                  *
*******************************************************************************

To build this sample webservice deployment you must:

1)   Have installed Java and Apache-ant version 1.6 or later.

2)  Refer to the readme in the wsc.sample.client.jar directory client/readme.txt
    to download and setup axis and rampart.
     
3)  If you want to debug into the axis2 sources, create a src directory under 
     your axis2_home directory and download the axis2-1.3-src.zip file into 
     that directory.
     
4)  In a command window, navigate to the root folder containing this 
    readme.txt file sample_webservice/.

5)  Copy the file sample.project.properties to project.properties

6)  Edit project.properties and set parameters (bb.home, axis2.home).  The 
    build depends on several Axis2 libraries, and the 
    Blackboard bb-platform.jar library.

7)  Build the sample webservice deployment file by executing command: 
    "ant" or "ant all"

8)  The generated sample webservice deployment location:
    sample_webservice/dist/lib/sampleWebservice.jar

*******************************************************************************
*  Section 4:             Eclipse Requirements                                *
*******************************************************************************

Steps to add the project to Eclipse.

1)  Import project named wss.sample.webservice located in the same directory 
    containing this readme.txt file.

2)  In eclipse, setup a classpath variable named AXIS2_HOME pointing to where 
    you unzipped axis2, which should be under:
    window/preferences/java/build path/classpath variables

3) In eclipse, setup a classpath variable named BB_HOME pointing to where you 
   deployed Blackboard, which should be under:
   window/preferences/java/build path/classpath variables

4) Eclipse should successfully compile the sample webservice project.


*******************************************************************************
*  Section 5:        Webservice types POJO vs. Service                        *
*******************************************************************************

 - This sample webservice source code as provided supports building both types.  
 - By default, it builds a POJO webservice.  
 - You can build a service by simply adding the services.xml file to this 
   directory jar_resources/META-INF/services.xml
 - The build script will detect the presence of the services.xml file and 
   build a service deployment file (sampleWebservice.aar)

*******************************************************************************
*  Section 6: Installing webservices and Building blocks in a single process  *
*******************************************************************************

Webservices can be packaged into a building block deployment file. These 
webservices will then be automatically installed when the building block is 
installed.  The webservice properties must be configured from the administrator 
panel - webservices UI.  Redeploying a Building block with webservices will 
deploy any updated webservices. Deleting a Building block will not delete any 
webservices.  These webservices can be removed using the administrator 
panel - webservices UI.
 
 Steps:
 
 1)  Add one or more webservice jars to the Building block deployment jar file.
 
 2)  Add the list of webservice jars to  Building block bb-manifest.xml. Below 
     is a snipet of a Building block bb-manifest.xml file containing 2 
     webservices to be installed. Note the NEW webservice element.
    
 3)  Upload the Building block.
 
 Example of bb-manifest.xml containing 2 webservices to be installed:
 =======================================================================

    <vendor>
      <id value="bb"/>
      <name value="Blackboard Inc."/>
      <url value="http://www.blackboard.com/" />
      <description value="Blackboard" />
    </vendor>
    
    <webservice>
      <filename value="sampleWebservice.jar">
      <filename value="sampleWebservice2.jar">
    </webservice>
    
    <http-actions>
      <config value="admin/config.jsp"/>
    </http-actions>

*******************************************************************************
*  Section 7:     Accessing webservices through Building Blocks               *
*******************************************************************************

A webservice can be accessed from a Building Block through a Factory. The 
deployment, redeployment, and undeployment of a service requires restarting 
Tomcat.

1) Create a Factory class in the directory containing the webservice source code.

2) Copy the webservice jar file to the tomcat lib directory, 
   blackboard/apps/tomcat/lib
   
3) Restart Tomcat

Factory class example:
=====================

package sample.ws;

import blackboard.platform.ws.WebServiceWrapper;

/** This class is used by Building Block code that wants to use the user Web 
 * Service methods inside the Blackboard Learn VM.
 */
public class HelloWorldWSFactory
{
  public static HelloWorldWS getUserWS()
  {
    return (HelloWorldWS) WebServiceWrapper.newInstanceForBuildingBlock( new HelloWorldWSImpl(), "HelloWorldWS.WS" );
  }
}

********************************************************************************
*  Section 8: Removing a webservice that is accessed through a Building        *
*             block Factory                                                    *
********************************************************************************

1)  Optionally remove the webservice using the administrator panel - webservices 
    UI.  You may want to leave it accessible as a 'regular' webservice.

2)  Shutdown Tomcat.

3)  Manually remove the webservice deployment jar file from tomcat lib 
    directory blackboard/apps/tomcat/lib

4)  Restart Tomcat.

*******************************************************************************
* Section 9:        Resources Available on Edugarage                         *
*******************************************************************************

 - Proxy Tools and Web Service Clients
   http://www.edugarage.com/display/BBDN/Proxy+Tools+and+Web+Service+Clients

 - How to write basic Web Service Clients
   http://www.edugarage.com/display/BBDN/How+to+Write+Basic+Web+Service+Clients

 - How to build Proxy Tools
   http://www.edugarage.com/display/BBDN/How+to+Build+Proxy+Tools

 - Authenticating Proxy Tool Requests
   http://www.edugarage.com/display/BBDN/Proxy+Tool+SSO

 - Proxy Tool XML Description
   http://www.edugarage.com/display/BBDN/Proxy+Tool+XML+Description

