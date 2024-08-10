/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.rates;

import java.time.ZonedDateTime;
import java.util.List;

import org.unlockedlabs.pkrates.ws.api.core.RateUnavailableException;

/**
 * Parking Price class used for encapsulating the {@code price} for parking.
 * 
 * @author Richard Salas
 * @version 1.0.0
 */
public class ParkingPrice {

    private int price;

    /**
     * Constructor used for creating an instance of ParkingPrice using the given price.
     * 
     * @param price the price
     */
    public ParkingPrice(int price) {
        this.price = price;
    }//end constructor

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }//end method

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }//end method

    /**
     * Calculates parking price based on the given {@code startDtTm}, {@code endDtTm} and {@code parkingRatesByDay} and returns an instance of {@code ParkingPrice}.
     * 
     * @param startDtTm
     * @param endDtTm
     * @param parkingRatesByDay
     * @return
     * @throws RateUnavailableException
     */
    public static ParkingPrice calculatePrice(ZonedDateTime startDtTm, ZonedDateTime endDtTm, List<RateDO> parkingRatesByDay) throws RateUnavailableException {
        boolean parkingRateFound = false;
        ParkingPrice price = null;
        for (RateDO rateDO : parkingRatesByDay) {
            ZonedDateTime rateStartDtTm = rateDO.getRateZoneStartTm(startDtTm.toLocalDate());
            ZonedDateTime rateEndDtTm = rateDO.getRateZoneEndTm(endDtTm.toLocalDate());
            if(startDtTm.isAfter(rateStartDtTm) && startDtTm.isBefore(rateEndDtTm) && endDtTm.isAfter(rateStartDtTm) && endDtTm.isBefore(rateEndDtTm)) {
                if(parkingRateFound) {
                    throw new RateUnavailableException("User input spanned more than one parking rate.");
                }else{//found the rate
                    price = new ParkingPrice(rateDO.getPrice());
                    parkingRateFound = true;
                }//end if
            }//end if
        }//end for
        
        if(price==null) {
            throw new RateUnavailableException("No parking rate found based on user input.");
        }//end if

        return price;
    }//end method

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ParkingPrice [price=");
        builder.append(price);
        builder.append("]");
        return builder.toString();
    }//end method

}//end class
