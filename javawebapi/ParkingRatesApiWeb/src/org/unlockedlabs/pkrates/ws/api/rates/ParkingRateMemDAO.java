/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.rates;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Data Access Object used to execute Read/Write/Delete operations against the {@code PARKING_RATES} list.
 *  
 * @author Richard Salas
 * @version 1.0.0
 */
public class ParkingRateMemDAO {

    private static final Logger myLogger = Logger.getLogger("org.unlockedlabs.pkrates.ws.api.rates.ParkingRateMemDAO");

    private static List<RateDO> PARKING_RATES = Collections.synchronizedList(new ArrayList<>());//synchronized list for thread safety 

    static {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("INIT.jsonFilePath")));//read file in
            RateModel rates = new ObjectMapper().readValue(new String(bytes), RateModel.class);//convert string into RateModel
            List<RateDO> rateDOList = rates.getRates().stream().map(rateDTO -> rateDTO.toDO()).collect(Collectors.toList());//convert DTOs into DOs
            PARKING_RATES.addAll(rateDOList);//add
        }catch(Exception e) {
            myLogger.error("Exception occurred during an attempt to initially load parking rates from a file. Values of interest are INIT.jsonFilePath=" + System.getProperty("INIT.jsonFilePath") + ";PARKING_RATES=" + String.valueOf(PARKING_RATES) + " Error message is: " + e.getMessage(), e);
        }//end try...catch
    }//end static block

    /**
     * Constructor used to create an instance of ParkingRateMemDAO.
     */
    public ParkingRateMemDAO() { }//end method

    /**
     * Reloads the {@code PARKING_RATES} list using the given {@code newRates} list.
     * 
     * @param newRates the list used to replace what is within the PARKING_RATES list
     */
    public void reloadRates(List<RateDO> newRates) {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering reloadRates() which is used for reloading the PARKING_RATES list using the given newRates list. Incoming parameter is newRates=" + String.valueOf(newRates));
        }//end if

        PARKING_RATES.clear();
        PARKING_RATES.addAll(newRates);

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting reloadRates() method. No return value.");
        }//end if
    }//end method

    /**
     * This method returns the gets parking rates
     * 
     * @return an instance of the current list of parking rates 
     */
    public List<RateDO> getParkingRates() {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering getParkingRates() which is used for returning a list of current parking rates. No incoming parameters.");
        }//end if

        List<RateDO> returnList = new ArrayList<>(PARKING_RATES);

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting getParkingRates() method. Return value is returnList=" + String.valueOf(returnList));
        }//end if
        return returnList;
    }//end method

}//end class
