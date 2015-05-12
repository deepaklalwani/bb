*******************************************************************************
*                          Table of Contents                                  *
*******************************************************************************

 Section 1: Client Directory Information
 Section 2: About
 Section 3: bb-manifest.xml
 Section 4: Webservice types POJO vs. Service
 Section 5: Accessing webservices through Building Blocks
 Section 6: Resources Available on Edugarage

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
for Java developers interested in creating their own web services.

*******************************************************************************
*  Section 3              bb-manifest.xml                                     *
*******************************************************************************

Each webservice has to provide a META-INF/bb-manifest.xml file describing the 
webservice. It should be similar to the following:

<?xml version="1.0" encoding="ISO-8859-1"?>
<manifest>
<webservice>
<name value="Sample.WS"/>   
<description value="Description of your webservice"/>
<permissions>
<permission type="attribute" name="user.*" actions="get,set"/>
</permissions>
</webservice>
</manifest>

Where the format of the <permissions> element is the same as what you would 
develop for a building block.

*******************************************************************************
*  Section 4:        Webservice types POJO vs. Service                        *
*******************************************************************************

 - This sample webservice source code as provided supports building both types.  
 - By default, it builds a POJO webservice.  
 - You can build a service by simply adding the services.xml file to this 
   directory jar_resources/META-INF/services.xml
 - The build script will detect the presence of the services.xml file and 
   build a service deployment file (sampleWebservice.aar)

*******************************************************************************
*  Section 5:     Accessing webservices through Building Blocks               *
*******************************************************************************

A webservice can be access from a Building Block via a Factory. The deployment, 
redeployment, and undeployment of a service requires restarting Tomcat.

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

*******************************************************************************
* Section 6:         Resources Available on Edugarage                         *
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

