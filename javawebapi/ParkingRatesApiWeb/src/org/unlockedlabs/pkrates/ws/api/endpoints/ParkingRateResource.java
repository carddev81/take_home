/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.endpoints;

import java.time.ZonedDateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.unlockedlabs.pkrates.ws.api.core.RateUnavailableException;
import org.unlockedlabs.pkrates.ws.api.rates.ParkingPrice;
import org.unlockedlabs.pkrates.ws.api.rates.RateModel;
import org.unlockedlabs.pkrates.ws.api.rates.ParkingRateService;

/**
 * Parking Rate Resource web service API endpoints used for uploading and retrieving data about parking rates.
 *
 * @author Richard Salas
 * @version 1.0.0
 */
@Path("/")
public class ParkingRateResource {

    private static Logger myLogger = Logger.getLogger("org.unlockedlabs.pkrates.ws.api.endpoints.ParkingRateResource");

    /**
     * Gets a list of parking rates.
     * 
     * @return response the response containing the list of parking rates as JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rates")
    public Response getRates() {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering getRates() method is a webservice endpoint used for returning a list of parking rates.  No incoming parameters.");
        }//end if

        Response response = null;
        try{
            ParkingRateService service = new ParkingRateService();
            RateModel rates = service.getParkingRates();
            response = Response.status(200).entity(rates).build();//building response
        }catch(Exception e){
            myLogger.error("Exception occurred during an attempt to retrieve a list of rates.  Error message is: " + e.getMessage(), e);
            response =  Response.status(Response.Status.BAD_REQUEST).build();
        }//end try...catch

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting getRates() method. Return value is: response=" + String.valueOf(response));
        }// end if
        return response;
    }//end method

    /**
     * Saves a list of parking rates using the incoming JSON parking {@code rates} .
     * 
     * <p>NOTE This service will overwrite the currently stored rates with the given rates</p>
     * 
     * @return response the response letting the user know rates were successfully updated
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("rates")
    public Response saveRates(RateModel rates) {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering saveRates() method is a webservice endpoint used to update rates using the given rates parameter parsed from JSON.  Incoming parameter is rates=" + String.valueOf(rates));
        }//end if

        Response response = null;
        try{
            ParkingRateService service = new ParkingRateService();
            service.updateRates(rates.getRates());
            response = Response.status(200).entity("Successfully updated parking rates").build();//build response
        }catch(Exception e){
            myLogger.error("Exception occurred during an attempt to update the parking rates.  Error message is: " + e.getMessage() + ". Value of interest is:  rates=" + String.valueOf(rates), e);
            response =  Response.status(Response.Status.BAD_REQUEST).build();
        }//end try...catch

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting saveRates() method. Return value is: response=" + String.valueOf(response));
        }// end if
        return response;
    }//end method

    /**
     * Gets a parking rate price based on the given {@code startDtTm} and {@code endDtTm}.
     * 
     * @return response the response containing the price as JSON or an unavailable response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("price")
    public Response getPrice(@QueryParam("start") ZonedDateTime startDtTm, @QueryParam("end") ZonedDateTime endDtTm) {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering getPrice() method is a webservice endpoint used for returning a parking rate price using the given startDtTm and endDtTm parameters.  Incoming parameters are startDtTm=" + String.valueOf(startDtTm) + "; endDtTm=" + String.valueOf(endDtTm));
        }//end if

        Response response = null;
        try{
            ParkingRateService service = new ParkingRateService();
            ParkingPrice price = service.getPriceByTime(startDtTm, endDtTm);
            response = Response.status(200).entity(price).build();//building response
        }catch(RateUnavailableException e){//"unavailable"
            myLogger.error("RateUnavailableException occurred while getting price based on user date/time input.  Valus of interest are: startDtTm=" + String.valueOf(startDtTm) + "; endDtTm=" + String.valueOf(endDtTm) +"; Error message is: " + e.getMessage(), e);
            response =  Response.status(Response.Status.OK).entity("unavailable").build();
        }catch(Exception e){
            myLogger.error("Exception occurred while getting price based on user date/time input.  Valus of interest are: startDtTm=" + String.valueOf(startDtTm) + "; endDtTm=" + String.valueOf(endDtTm) +"; Error message is: " + e.getMessage(), e);
            response =  Response.status(Response.Status.BAD_REQUEST).build();
        }//end try...catch

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting getPrice() method. Return value is: response=" + String.valueOf(response));
        }// end if
        return response;
    }//end method

}//end class
