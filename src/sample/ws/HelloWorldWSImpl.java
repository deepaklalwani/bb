package sample.ws;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;

import java.io.*;
import blackboard.platform.filesystem.FileSystemService;
import blackboard.platform.BbServiceManager;
import blackboard.data.course.Course;
import blackboard.platform.filesystem.FileSystemException;
import java.util.*;

import blackboard.cms.filesystem.*;

import blackboard.platform.ws.*;
import blackboard.platform.ws.anns.AuthenticatedMethod;
import blackboard.platform.ws.anns.FinalMethodDoNotChange;
import blackboard.platform.ws.anns.FinalWebServiceMethodsDoNotChange;

@WebService(name = "HelloWorld", serviceName = "HelloWorld", portName = "WS")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@FinalWebServiceMethodsDoNotChange(methods = { "1:hello","1:helloAuthenticated" }, releaseDates = { "1:Release 9.1 Jan. 2009" })
public class HelloWorldWSImpl implements HelloWorldWS
{  
  private static final long CURRENT_HELLOWORLD_WS_VERSION = 1;
  private static final String THIS_WS_NAME = "HelloWorld";

  private static final String CONTENT = "content";
  private static final String FILE_SEPARATOR = System.getProperty("file.separator");
  private WebserviceLogger logger = WebserviceLogger.getInstance();

  @WebMethod(action = "getServerVersion")
  @WebResult(name = "serverVersion")
  @FinalMethodDoNotChange(parameterTypes = { "blackboard.platform.ws.VersionVO" }, returnType = "blackboard.platform.ws.VersionVO", since = "1")
  public VersionVO getServerVersion( @WebParam(mode = WebParam.Mode.IN, name = "unused") VersionVO unused )
  {
    return new VersionVO( CURRENT_HELLOWORLD_WS_VERSION );
  }

  @WebMethod(action = "initializeCourseWS")
  @WebResult(name = "session")
  @FinalMethodDoNotChange(parameterTypes = { "boolean" }, returnType = "boolean", since = "1")
  public boolean initializeHelloWorldWS( boolean ignore )
  {
    SessionVO session = WebserviceContext.getCurrentSession();
    session.setClientWebServiceVersion( THIS_WS_NAME, 1 );
    return true;
  }
/* Deepak Not returing any data ... Permission Error   
  @WebMethod(action = "hello")
  @FinalMethodDoNotChange(parameterTypes = { "java.lang.String" }, returnType = "java.lang.String", since = "1")
  public String hello( String name )
  {

    FileSystemService fileService = BbServiceManager.getFileSystemService();
    File courseDirectory,file=null,file1=null;
    File contentDirectory = null;
    Course course = new Course();
    
    try {
      course.setCourseId("PRAGYA-COURSE-2");
      //course.setId("_43_1");
      courseDirectory = fileService.getCourseDirectory(course);
      contentDirectory = new File(courseDirectory+FILE_SEPARATOR+CONTENT);
      file = findFile(contentDirectory,"Next-Fix");
      file1 = findFile(contentDirectory,"pragyatest");

    } catch (FileSystemException e) {
      
      System.err.println("Error getting content path for course :"+course.getCourseId());
    }

    return "Hello "+name +".  This method did not require a valid session."+contentDirectory+"  > "+file+ " >> "+file1;
    

 //   return "Hello "+name +".  This method did not require a valid session.";
  }

    private File findFile(File rootDirectory, String fileName)
  {
        if(rootDirectory == null || fileName == null || !rootDirectory.isDirectory())
        {
            return null;
        }

        File[] files = rootDirectory.listFiles();

        for(int f=0; files != null && f < files.length; f++)
        {
            if(files[f] == null) { continue; }

            if(files[f].isDirectory())
            {
                File file = findFile(files[f], fileName);

                if(file != null)
                {
                    return file;
                }
            }
            else
            {
                if(files[f].getName() != null && files[f].getName().equals(fileName))
                {
                    return files[f];
                }
            }
        }
        return null;
  }
*/

  @WebMethod(action = "hello")
  @FinalMethodDoNotChange(parameterTypes = { "java.lang.String" }, returnType = "java.lang.String", since = "1")
  public String hello( String name )
  {
    logger.logError("hello Start");
    Properties prop = null;
    Course course = new Course();
    try {
      course.setCourseId("PRAGYA-COURSE-2");
      prop = getContentProperties("contentids_Boone.txt", course);

    } catch (Exception e) {
      System.err.println("Error getting content path for course :"+course.getCourseId());
    }

    return "Hello "+name +".  This method did not require a valid session."+prop.getProperty("contentIDs");
    

 //   return "Hello "+name +".  This method did not require a valid session.";
  }

  public Properties getContentProperties(String fileName, Course course)
  { 
    logger.logError("getContentProperties Start");
    Properties p = new Properties();
    try {
      InputStream is = null;
      CSContext ctxCS = CSContext.getContext();
      CSFile csFile = (CSFile)ctxCS.findEntry("/courses/" + course.getCourseId() + "/" + fileName);
      
      logger.logError("getContentProperties csFile : "+csFile);

      logger.logError(">>>>>>>>>>>>> csFile : "+ctxCS.findEntry("/courses"));
      logger.logError(">>>>>>>>>>>>> csFile : "+ctxCS.findEntry("/usr/local/blackboard/courses"));


      if (csFile == null) {
        CSDirectory csd = ctxCS.getCourseDirectory(course);
        logger.logError("getContentProperties CSDirectory : "+csd);
        csFile = (CSFile)findFileEntry(csd, fileName);
        logger.logError("getContentProperties csFile : "+csFile);
      }

      ByteArrayOutputStream outFile = new ByteArrayOutputStream();
      if (csFile != null) {
        csFile.getFileContent(outFile);
      }
      is = new ByteArrayInputStream(outFile.toByteArray());
      p.load(is);
      is.close();
      outFile.close();
    } catch (Exception e) {
      logger.logError("Error In Loading ContentID File" + e);
    }
    return p;
  }

  public static CSEntry findFileEntry(CSDirectory csDirectory, String fileName)
  {
    if ((csDirectory == null) || (fileName == null)) {
      return null;
    }

    List files = csDirectory.getDirectoryContents();
    for (int f = 0; (files != null) && (f < files.size()); f++) {
      CSEntry cse = (CSEntry)files.get(f);
      if (cse == null) {
        continue;
      }
      if ((cse instanceof CSDirectory)) {
        CSDirectory newCsd = (CSDirectory)cse;
        CSEntry cseNew = findFileEntry(newCsd, fileName);
        if (cseNew != null) {
          return cseNew;
        }
      }
      else if ((cse != null) && (cse.getBaseName().equals(fileName))) {
        return cse;
      }
    }

    return null;
  }


  @AuthenticatedMethod(entitlements={})
  @WebMethod(action = "helloAuthenticated")
  @FinalMethodDoNotChange(parameterTypes = { "java.lang.String" }, returnType = "java.lang.String", since = "1")
  public String helloAuthenticated( String name )
  {
    return "Hello "+name+".  This method required an authenticated session.";
  }

  @AuthenticatedMethod(entitlements={"content.item.VIEW", "content.item.MODIFY" }, checkEntitlement = true )
  @WebMethod(action = "helloAuthenticatedWithEntitlementPrecheck")
  @FinalMethodDoNotChange(parameterTypes = { "java.lang.String" }, returnType = "java.lang.String", since = "1")
  public String helloAuthenticatedWithEntitlementPrecheck( String name )
  {
    return "Hello "+name+".  This method required an authenticated session with specific entitlements.  " +
    		"The entitlements were checked by the framework prior to execution.";
  }

  @AuthenticatedMethod(entitlements={"content.item.VIEW", "content.item.MODIFY" } )
  @WebMethod(action = "helloAuthenticatedWithEntitlements")
  @FinalMethodDoNotChange(parameterTypes = { "java.lang.String" }, returnType = "java.lang.String", since = "1")
  public String helloAuthenticatedWithEntitlements( String name )
  {
    return "Hello "+name+".  This method required an authenticated session with specific entitlements.  " +
    "The entitlements however, were NOT checked by the framework prior to execution.  It relys on underlying " +
    "functionality to verify the session contains the required entitlements.";
  }
}
