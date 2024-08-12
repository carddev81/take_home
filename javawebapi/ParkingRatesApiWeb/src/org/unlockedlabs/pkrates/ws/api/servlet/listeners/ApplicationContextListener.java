/**
 *
 */
package org.unlockedlabs.pkrates.ws.api.servlet.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.unlockedlabs.pkrates.ws.api.rates.ParkingRateMemDAO;

/**
 * This ApplicationContextListener class is for receiving notification events about ServletContext lifecycle changes.
 * 
 * <p>This class handles 2 lifecycle changes.  They are <b>context destroyed</b> which occurs on shutdown of the web application and <b>context initialized</b> which occurs on start up of the web application. This <code>ApplicationContextListener</code> allows the application to release any system resources on shutdown or to obtain any resources needed on startup.</p>
 *
 * @author Richard Salas
 * @version 1.0.0
 */
public class ApplicationContextListener implements ServletContextListener {

    /**
     * Receives notification that the ServletContext is about to be shut down.
     * <p>All servlets and filters will have been destroyed before any ServletContextListeners are notified of context destruction.</p>
     * <p>This method is utilitzed for releasing any system resources that may be holding locks on any application logging files.</p>
     * @param sce the ServletContextEvent containing the ServletContext that is being destroyed
     */
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Entering contextDestroyed");
        try{
            System.out.println("Attempting to shutdown the log manager.");
            LogManager.shutdown();
            System.out.println("LogManager was successfully shutdown.");
        }catch(Exception e){
            System.err.println("Exception has occurred while trying to shutdown the LogManager. Error message is: " + e.getMessage());
        }//end try...catch
        System.out.println("Exiting contextDestroyed");
    }//end method

    /**
     * Receives notification that the web application initialization process is starting.
     * <p>All ServletContextListeners are notified of context initialization before any filters or servlets in the web application are initialized.</p>
     * @param sce the ServletContextEvent containing the ServletContext that is being initialized
     */
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Entering contextInitialized");
        // need to initialize the in-memory db (list).
        new ParkingRateMemDAO();
        System.out.println("Exiting contextInitialized");
    }//end method

}//end class
