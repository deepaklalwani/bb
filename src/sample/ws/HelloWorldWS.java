package sample.ws;

import blackboard.platform.ws.VersionVO;
import blackboard.platform.ws.anns.AuthenticatedMethod;

public interface HelloWorldWS
{  
  /* The purpose of the sample web service is to illustrate how to build a webservice that takes advantage of the 
   * Blackboard web service framework.
   * 
   * Creating a webservice:
   * How to generate a web service that will interact with the rest of the Learn webservice framework (also refer to 
   * the interfaces defined below and implementation code found in HelloWorldWSImpl.java).
   * 
   * 1) Implement an interface and define all your webservice methods in your interface. Our webservice framework 
   *    provides global functionality (logging, session validation, more TBD) via a proxy wrapper around your webservice 
   *    so having an interface is mandatory. 
   * 2) Declaring your class as a webservice: Add the following annotations to your class:
   *        @WebService(name="yourservice", serviceName="yourservice", portName="WS")
   *        @SOAPBinding(
   *            style=SOAPBinding.Style.DOCUMENT, 
   *            use=SOAPBinding.Use.LITERAL, 
   *            parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
   *            
   * 3) Declare each of your exposed methods as public
   * 4) Make sure each method is declared with no throws clause - all exceptions thrown should be via the method 
   *    AxisHelpers.throwWSException with a unique code identifying the error and freeform text of the message 
   *    describing the error condition for the client.  Each of these unique codes should be documented.
   * 5) Add the following annotations for each exposed method:
   *        @WebMethod(action="methodName")
   *            @WebResult(name="returnValue")
   *            public ReturnValue methodName(
   *  
   * 6) For each parameter, add an annotation as follows:
   *        @WebParam(mode=WebParam.Mode.IN, name="varName") VarType varName
   * 7) Add javadoc comments for your method (Do this in your interface definition) 
   * 8) Use WebserviceLogger.logInfo|logDebug|logError methods for all your logging. This will direct your logging
   *    to an appropriate file as define in the admin UI (i.e. per-service vs. common logging) 
   * 9) Each method that you need to be executed as an authenticated user needs to be annotated with:
   *        @AuthenticatedMethod(entitlements={list-of-entitlements-needed},checkEntitlement=(true|false))
   *    This must be done on BOTH the interface definition AND the implementation (The annotation is read from one 
   *    place at runtime to check authentication and from another while generating the 'operations' page in the UI). 
   *    The list of entitlements declared here is NOT actually checked at runtime unless you explicitly specify 
   *    checkEntitlement=true (default value is false). This information is used when generating the list of entitlements 
   *    to grant to a tool during registration when it declares that it is going to be calling this method .
   * 10) If you are working in the context of a given course, include a CourseIdVO object in your parameter list:
   *        @WebParam(mode=WebParam.Mode.IN, name="courseid") CourseIdVO courseId
   *     By adding this parameter the framework will setup this course on your threads context so your code will 
   *     execute with the appropriate entitlements set for the WS user within the specified course. 
   * 11) Each method must be atomic - you cannot persist data in-memory between any two calls. If call X needs an 
   *     artifact from call Y to succeed then you either must pass it through the client (in which case trust issues 
   *     come into play - do you trust that the client has not modified the data and if they have do you care) or you 
   *     must persist it in the database in a unique-to-your-service method. No framework is provided for 
   *     session-persistent-data.
   * 12) If possible, all parameters and return values should be primitive types. If you do have to return a complex type, 
   *     once you have defined it, you should use the @FinalObjectDoNotChange annotation on it to declare it final to the 
   *     build process.  NOTE you will need to extend your build process to check the @FinalObjectDoNotChange, 
   *     @FinalWebServiceMethodsDoNotChange, and @FinalObjectDoNotChange annotations.
   *     
   * 
   * Note that since we are utilizing WS-Security headers for session management, we require that the client program insert 
   * a UserToken with userid='session' and password=sessionid for each request. As such, each method does not need to take 
   * any session object as a parameter and can rely on the framework functionality to:
   *     o Validate that the session is correct (ie. not tampered with) and current (not timed out) 
   *     o Have appropriate entitlements set for the WS user on the current thread.
   *     o You still need the @AuthenticatedMethod if you want the framework to fail for you for 
   *     unauthenticated calls to the method. 
   * 
   * 
   * 
   * Versioning:
   * Every webservice should support the versioning framework from the initial release forward.  
   * Subsequent versions will implement a new initializeForVersionTwo, etc.
   * 
   * Versioning has been added as part of the new web services framework.  The purpose of versioning is to meet the 
   * following requirements:
   *    o Applications coded to the first release of the SDK must be able to work with newer server instances
   *    o Applications coded to newer releases of the SDK that only use functionality that existed in older releases 
   *      must be able to work.
   *      
   * To accomplish this, the following must be in place (or is implicitly in place based on the current SOAP/WebServices 
   * frameworks):
   *    o The server must know what version of the client it is dealing with so that it can adjust behaviour appropriately. 
   *    o The client must know what version of the server it is dealing with so that it can adjust behaviour appropriately. 
   *    o Once a method signature is officially released we can never change or delete the method 
   *    o Once a value object's structure is defined we can never change it in any way 
   * 
   * Reasoning for different function names for version initialization:
   * Protecting the client developer from themselves. If we had a generic initializeForVersion(long ver) method and a 
   * long getServerVersion(), developers thinking "argh - what version is this again? I'll just use the server version, 
   * and code initializeForVersion(getServerVersion()); 
   * This will always work for developers in their development environment because the two versions match, but then 
   * when its deploy on a system with a newer (or older) version, the client will fail to provide the correct version 
   * to the server.
   * 
   * Code Samples:
   *
   * As part of initial negotiation with any web service, the client program has to negotiate the level at which they 
   * will run via code similar to:
   * 
   *   boolean initialized = false;
   *   long serverVer = helloWorldsdk.getServerVersion(unused);
   *   if (serverVer >= 2) { // hard-coded based on when the client program was physically created and the sdk it 
   *                            was coded against
   *     initialized = helloWorldsdk.initializeForVersionTwo();
   *   } else { // server is version 1 only
   *     initialized = helloWorldsdk.initialize();
   *   }
   *   
   * Creating a new version of a webservice
   * As described above, the developer implemented the webservice methods getServerVersion and initialize. This forces 
   * all client applications to declare themselves as having been written against the 'version 1' version of your 
   * webservice. Now that you need to make changes to the webservice you have to make sure that all those clients will 
   * still work the way they were documented, even if you have a need to change the semantics of an existing method.
   * 
   * Steps:
   *   1) Increment your constant for CURRENT_YOURSERVICE_WS_VERSION 
   *   2) Add a new initializeForVersionTwo/Three/xxx method
   *   3) Implement new methods 
   *   4) IF you need to change the behaviour of an existing method, you must retain the existing behaviour. Your code 
   *   will look like this: 
   *       if (session.getClientVersion(THIS_WS_NAME) == 1) 
   *       {
   *         // Existing version 1 code
   *         } else {
   *         // new version 2 code
   *       }
   *       
   * Best practice is not to restrict the execution of a new method based on the declared client version unless you 
   * need specific logic as shown above. In other words, if you add a methodX in version 3, do not disallow calling 
   * the method if the client is declared as version 1. This way a client developer can declare that they want to behave 
   * like version 1, but if they decide to call version 3 methods they will still work. This is just a 'rule of thumb' 
   * though - if you have an exceptional case where you really, really don't want a version 1 client to be allowed 
   * to call your method then you could add this code, but it is frowned upon.
   * 
   */
  
  /**
   * Returns the current version of this web service on the server
   * @param unused - this is an optional parameter put here to make the generation of .net client
   *                 applications from the wsdl 'cleaner' (0-argument methods do not generate clean stubs and
   *                 are much harder to have the same method name across multiple Web Services in the same .net client)
   * @since 1 
   */
  public VersionVO getServerVersion( VersionVO unused );

  /**
   * sets the client version to version 1 and returns an appropriate session. 
   * With each release of this web service implement a new initializeVersionXXX method 
   * @param ignore - this is an optional parameter put here to make the generation of .net client
   *                 applications from the wsdl 'cleaner' (0-argument methods do not generate clean stubs and
   *                 are much harder to have the same method name across multiple Web Services in the same .net client)
   * @return true to indicate that the session has been initialized for the util ws
   * @since 1
   */
  public boolean initializeHelloWorldWS( boolean ignore );
  
  
  
  /*
   * The Blackboard web service framework has added several annotations to the Building Blocks API.  
   * The annotations are AuthenticatedMethod, FinalMethodDoNotChange, FinalObjectDoNotChange, 
   * and FinalWebServiceMethodsDoNotChange.  These have been documented in the Building Blocks API 
   * javadocs.  
   * 
   * Use the 'entitlements()' annotation on your webservice methods to declare the Academic Suite 
   * entitlements that the current session will need to successfully call your method.  
   * 
   * NOTE that the framework will not pre-check these entitlements unless you set checkEntitlement=true.
   * If checkEntitlement is false or not specified then this list is used to ease tool registration only.  
   * In that case, it is your responsibility to check the entitlements explicitly so that you can react
   * appropriately (maybe you want to act 'differently' if the method is called without the needed 
   * entitlement instead of failing with a security exception)
   * 
   */

  /**
   * Example of a method not requiring an authenticated session, nor entitlements.
   * @since 1
   */
  public String hello(String name);
  
  /**
   * Example of a method requiring a valid login session to execute.  
   * The "@AuthenticatedMethod" annotation requires a valid session to function properly.
   * 
   * NOTE that this must be applied twice: First on the web service interface and then again
   * on the implementation of the concrete method.  The interface is required for runtime
   * authentication validation to work and the concrete method is required for the
   * 'operations' page in the UI.
   * 
   * Use the 'entitlements()' annotation on your webservice methods to declare the Academic Suite 
   * entitlements that the current session will need to successfully call your method
   * 
   * @return formatted string containing provided name and a description of the method called.
   * @since 1
   */
  @AuthenticatedMethod(entitlements={})
  public String helloAuthenticated(String name);
  
  /**
   * Example of a method requiring a valid login session that also has two entitlements.  The framework
   * will NOT pre-check that the session has the proper entitlements.   
   * 
   * NOTE that the framework will not pre-check these entitlements unless you set checkEntitlement=true.
   * If checkEntitlement is false or not specified then this list is used to ease tool registration only.  
   * In that case, it is your responsibility to check the entitlements explicitly so that you can react
   * appropriately (maybe you want to act 'differently' if the method is called without the needed 
   * entitlement instead of failing with a security exception)
   * 
   * @return formatted string containing provided name and a description of the method called.
   * @since 1
   */
  @AuthenticatedMethod(entitlements={"content.item.VIEW", "content.item.MODIFY" } )
  public String helloAuthenticatedWithEntitlements(String name);
  
  /**
   * Example of a method requiring a valid login session that also has two entitlements.  The framework
   * will pre-check that the session has the proper entitlements.   
   * 
   * NOTE that the framework will not pre-check these entitlements unless you set checkEntitlement=true.
   * If checkEntitlement is false or not specified then this list is used to ease tool registration only.  
   * In that case, it is your responsibility to check the entitlements explicitly so that you can react
   * appropriately (maybe you want to act 'differently' if the method is called without the needed 
   * entitlement instead of failing with a security exception)
   * 
   * @return formatted string containing provided name and a description of the method called.
   * @since 1
   */
  @AuthenticatedMethod(entitlements={"content.item.VIEW", "content.item.MODIFY" }, checkEntitlement = true )
  public String helloAuthenticatedWithEntitlementPrecheck(String name);
  
}
