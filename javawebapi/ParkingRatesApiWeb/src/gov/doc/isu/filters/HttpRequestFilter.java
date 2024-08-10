package gov.doc.isu.filters;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Class: HttpRequestFilter.java Date: Jan 12, 2006 Description:
 * <p>
 * Filter to log performance issues.
 * </p>
 *
 * @author Michael R. Dirks,Dwayne T. Walker
 * @author Andrew Fagre JCCC
 * @author Richard Salas JCCC
 * @version 1.1.0
 */
public class HttpRequestFilter implements Filter {
    private static Logger logger = Logger.getLogger("gov.doc.isu.filters.HttpRequestFilter");
    private FilterConfig filterConfig;

    /**
     * <p>
     * Called by the web container to indicate to a filter that it is being placed into service.
     * </p>
     * <p>
     * The servlet container calls the init method exactly once after instantiating the filter. The init method must complete successfully before the filter is asked to do any filtering work.
     * </p>
     * <p>
     * The web container cannot place the filter into service if the init method either
     * </p>
     * <ol>
     * <li>Throws a ServletException</li>
     * <li>Does not return within a time period defined by the web container</li>
     * </ol>
     *
     * @param config
     *        the filter configuration object used by the servlet container to pass information to a filter during initialization.
     * @throws ServletException
     *         general exception the servlet throws when it encounters difficulty.
     */
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }// end method

    /**
     * Performs the HTTP request filter action that displays the HTTP request information in the system output log.
     *
     * @param req
     *        HTTP Request Object
     * @param resp
     *        HTTP Response Object
     * @param chain
     *        filter chain
     * @throws javax.servlet.ServletException
     *         exeption that can be thrown by this method
     * @throws java.io.IOException
     *         exeption that can be thrown by this method
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        logger.debug("Entering doFilter()");
        HttpServletRequest request = (HttpServletRequest) req;

        // getting the ip of the users machine
        String ipAddress = (null != request.getHeader("X-Forwarded-For") ? request.getHeader("X-Forwarded-For") : request.getRemoteAddr());

        // Store the system timestamp so that we have a fairly unique
        // identifier for requests that come in
        // that we can use for tracking in the log
        String systemTimestamp = new SimpleDateFormat("[MM/dd/yyyy hh:mm:ss.SSS]").format(new Date());

        // Store the system timestamp as a date value also so that we can
        // record how many milliseconds each transaction takes
        java.util.Date start = new java.util.Date();

        // Store the request URL so that we can associate that with the
        // response time to
        // easily see which transactions are responding poorly.
        String urlString = request.getRequestURL().toString().concat(getHttpRequestMethodParameter(request));

        // Write the details of the request to the output log
        try{
            // Allow the request to be processed by the web application
            chain.doFilter(req, resp);
        }catch(ServletException e){
            StringBuffer sb = new StringBuffer("A ServletException was caught in the HttpRequestFilter doFilter() method. Exception Message: ").append(e.getMessage()).append(". Some Details follow: ").append("[request=").append(isNull(request)).append(", ipAddress=").append(isNull(ipAddress)).append(", userId=null").append(", systemTimestamp=").append(isNull(systemTimestamp)).append(", start=").append(isNull(start)).append(", urlString=").append(isNull(urlString)).append("]");
            logger.error(sb.toString(), e);
            throw e;
        }catch(IOException e){
            StringBuffer sb = new StringBuffer("An IOException was caught in the HttpRequestFilter doFilter() method. Exception Message: ").append(e.getMessage()).append(". Some Details follow: ").append("[request=").append(isNull(request)).append(", ipAddress=").append(isNull(ipAddress)).append(", userId=null").append(", systemTimestamp=").append(isNull(systemTimestamp)).append(", start=").append(isNull(start)).append(", urlString=").append(isNull(urlString)).append("]");
            logger.error(sb.toString(), e);
            throw e;
        }finally{
            // stop the stop watch
            long milliseconds = new java.util.Date().getTime() - start.getTime();

            // Show that the request has completed processing
            StringBuffer logBuffer = new StringBuffer("Processing complete for HTTP request, ").append(isNull(systemTimestamp)).append(", [").append(milliseconds).append(" ms], [").append(isNull(urlString)).append("], [IP Address: ").append(isNull(ipAddress)).append("], [UserId: null").append("]");
            logger.info(logBuffer.toString());
            logger.debug("Exiting doFilter()");
        }// end try...catch
    }// end method

    /**
     * Used to extract the Method name from HttpRequest calls. This is used for Applications Utilizing DispatchAction.
     *
     * @param request
     *        the HttpServletRequest
     * @return String value of the method name called in this Request.
     */
    private String getHttpRequestMethodParameter(HttpServletRequest request) {
        logger.debug("Entering getHttpRequestMethodParameter()");
        StringBuffer result = new StringBuffer();
        if(request.getParameterNames() != null){
            for(Enumeration<String> parameters = request.getParameterNames();parameters.hasMoreElements();){
                String name = parameters.nextElement();
                // Check if the parameter"name" == "method"
                if("method".equals(name)){
                    result.append(" ").append(name).append(": ");
                    result.append(collapseArray(request.getParameterValues(name)));
                } // end if
            } // end for
        }
        logger.debug("Exiting getHttpRequestMethodParameter()");
        return result.toString();
    } // end method

    /**
     * This method will check to see if an object is null and return it's String value.
     *
     * @param object
     *        object to return as a string value
     * @return string value of the object passed into this method
     */
    private String isNull(Object object) {
        logger.debug("Entering isNull()");
        logger.debug("Exiting isNull()");
        return object == null ? "null" : String.valueOf(object);
    }// end method

    /**
     * Called by the web container to indicate to a filter that it is being taken out of service.
     * <p>
     * This method is only called once all threads within the filter's doFilter method have exited or after a timeout period has passed. After the web container calls this method, it will not call the doFilter method again on this instance of the filter.
     * </p>
     * <p>
     * This method gives the filter an opportunity to clean up any resources that are being held (for example, memory, file handles, threads) and make sure that any persistent state is synchronized with the filter's current state in memory.
     * </p>
     */
    public void destroy() {
        this.filterConfig = null;
    }// end method

    /**
     * <p>
     * Converts an array of Strings to a comma separated line
     *
     * @param obj
     *        The Object[] to convert
     * @return String object
     */
    private String collapseArray(Object[] obj) {
        String retStr = "";
        if(obj == null || obj.length == 0){
            return retStr;
        }else{
            StringBuffer sb = new StringBuffer();
            for(int i = 0;i < obj.length;i++){
                if(obj[i] instanceof String){
                    sb.append((String) obj[i]);
                }else if(obj[i] instanceof Thread){
                    sb.append((Thread) obj[i]);
                }else if(obj[i] instanceof Integer){
                    sb.append((Integer) obj[i]);
                }else if(obj[i] instanceof Double){
                    sb.append((Double) obj[i]);
                }else if(obj[i] instanceof Long){
                    sb.append((Long) obj[i]);
                }else if(obj[i] instanceof BigDecimal){
                    sb.append((BigDecimal) obj[i]);
                }else if(obj[i] instanceof java.sql.Date){
                    sb.append((java.sql.Date) obj[i]);
                }else if(obj[i] instanceof java.util.Date){
                    sb.append((java.util.Date) obj[i]);
                }else if(obj[i] instanceof Time){
                    sb.append((Time) obj[i]);
                }else{
                    sb.append((String) obj[i]);
                }// end if/else
                 // Add other Object instances as needed.
                if(i + 1 < obj.length){
                    sb.append(",");
                }// end if
            }// end for
            retStr = sb.toString();
        }// end if/else
        return retStr;
    }// end collapseArray

    /**
     * @return the filterConfig
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * @param filterConfig the filterConfig to set
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
}// end class
