/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.rates;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.unlockedlabs.pkrates.ws.api.core.RateUnavailableException;

/**
 * Parking rate service used for handling the parking rate business operations.
 *
 * @author Richard Salas
 * @version 1.0.0
 */
public class ParkingRateService {

    private static final Logger myLogger = Logger.getLogger("org.unlockedlabs.pkrates.ws.api.rates.ParkingRateService");

    /**
     * Constructor used to create an instance of ParkingRateService
     */
    public ParkingRateService() { }//end constructor

    /**
     * Gets a list of parking rates and returns a {@code RateModel} instance which the rates will be encapsulated within.
     * 
     * @return returnRates the {@code RateModel} instance containing the parking rates
     */
    public RateModel getParkingRates() {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering getParkingRates() which is used for getting a list of current parking rates. No incoming parameter.");
        }//end if

        ParkingRateMemDAO dao = new ParkingRateMemDAO();

        RateModel returnRates = new RateModel();
        List<RateDTO> rateDTOList = dao.getParkingRates().stream().map(rateDO -> rateDO.toDTO()).collect(Collectors.toList());
        returnRates.setRates(rateDTOList);

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting getParkingRates() method. Return value is returnRates=" + String.valueOf(returnRates));
        }//end if
        return returnRates;
    }//end method

    /**
     * Updates the currently saved parking rates with the incoming {@code theRates} list.
     * 
     * <p>NOTE This service will overwrite the currently stored rates with the given rates</p>
     * 
     * @param theRates the list of {@code RateDTO}s that will be saved
     */
    public void updateRates(List<RateDTO> theRates) {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering updateRates() which is used for updating a list of parking rates. Incoming parameter is theRates=" + String.valueOf(theRates));
        }//end if

        List<RateDO> rateDOList = theRates.stream().map(rateDTO -> rateDTO.toDO()).collect(Collectors.toList()); 

        ParkingRateMemDAO dao = new ParkingRateMemDAO();
        dao.reloadRates(rateDOList);

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting getParkingRates() method. No return value.");
        }//end if
    }//end method

    /**
     * Gets the parking rate price using the given {@code startDtTm} and {@code endDtTm}.
     * 
     * @param startDtTm the start {@code ZonedDateTime} instance
     * @param endDtTm the end {@code ZonedDateTime} instance
     * @return returnPrice the {@code ParkingPrice} instance
     * @throws RateUnavailableException the exception that can be thrown if a rate is not found {@link RateUnavailableException}
     */
    public ParkingPrice getPriceByTime(ZonedDateTime startDtTm, ZonedDateTime endDtTm) throws RateUnavailableException{
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering getPriceByTime() which is used for getting a parking rate price using the given startDtTm and endDtTm. Incoming parameters are startDtTm=" + String.valueOf(startDtTm) + "; endDtTm=" + String.valueOf(endDtTm));
        }//end if

        Period m = Period.between(startDtTm.toLocalDate(),endDtTm.toLocalDate());
        if(!m.isZero()) {//1) check if date passed in spans more than a day and if so send unavailable (error)
            throw new RateUnavailableException("User input spanned more than a day.");
        }//end if

        //2)Get RateDO list based by day!
        Day day = Day.getAbbrevEnum(startDtTm.getDayOfWeek());
        List<RateDO> parkingRatesByDay = getParkingRatesByDay(day);
        ParkingPrice returnPrice = ParkingPrice.calculatePrice(startDtTm, endDtTm, parkingRatesByDay);

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting getPriceByTime() method. Return value is returnPrice=" + String.valueOf(returnPrice));
        }//end if
        return returnPrice;
    }//end method

    /**
     * Helper method used to get a list of parking rates using the given {@link Day}.
     * 
     * @param day the day used to find a list of parking rates
     * @return ratesByDayList the list of {@code RateDO}s found
     */
    private List<RateDO> getParkingRatesByDay(Day day) {
        if(myLogger.isDebugEnabled()){
            myLogger.debug("Entering getParkingRatesByDay() which is used to get a list of parking rates using the given day. Incoming parameter is day=" + String.valueOf(day));
        }//end if

        ParkingRateMemDAO dao = new ParkingRateMemDAO();
        List<RateDO> allRates = dao.getParkingRates();
        List<RateDO> ratesByDayList = allRates.stream().filter(rate -> rate.getDays().contains(day)).collect(Collectors.toList());

        if(myLogger.isDebugEnabled()){
            myLogger.debug("Exiting getParkingRatesByDay() method. Return value is ratesByDayList=" + String.valueOf(ratesByDayList));
        }//end if
        return ratesByDayList;
    }//end method

}//end class

