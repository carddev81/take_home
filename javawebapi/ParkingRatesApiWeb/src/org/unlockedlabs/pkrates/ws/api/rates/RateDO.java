package org.unlockedlabs.pkrates.ws.api.rates;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.unlockedlabs.pkrates.ws.api.core.IDTO;

/**
 * Persistent class stored within the memory data source.
 * 
 * @author Richard Salas
 * @version 1.0.0
 */
public class RateDO implements Serializable, IDTO<RateDTO> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<Day> days;
    private TimeRange times;
    private TimeZone tz;
    private int price;
    
    /**
     * Constructor used to create an instance of RateDO.
     */
    public RateDO() {
        
    }//end constructor

    /**
     * @return the days
     */
    public List<Day> getDays() {
        return days;
    }//end method

    /**
     * @param days the days to set
     */
    public void setDays(List<Day> days) {
        this.days = days;
    }//end method

    /**
     * @return the times
     */
    public TimeRange getTimes() {
        return times;
    }//end method

    /**
     * @param times the times to set
     */
    public void setTimes(TimeRange times) {
        this.times = times;
    }//end method

    /**
     * @return the tz
     */
    public TimeZone getTz() {
        return tz;
    }//end method

    /**
     * @param tz the tz to set
     */
    public void setTz(TimeZone tz) {
        this.tz = tz;
    }//end method

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
     * {@inheritDoc}
     */
    @Override
    public RateDTO toDTO() {
        RateDTO rateDTO = new RateDTO();
        //separate the days with commas
        List<String> commaSeparated = this.days.stream().map(s -> s.toString().toLowerCase()).collect(Collectors.toList());
        rateDTO.setDays(String.join(",", commaSeparated));
        rateDTO.setTimes(this.times.toString());
        rateDTO.setTz(this.tz.toZoneId().toString());
        rateDTO.setPrice(this.price);
        return rateDTO;
    }//end method

    /**
     * Gets the rate zone start data/time using the given {@code LocalDate}
     * 
     * @param theDate the local date to use for returning the ZonedDateTime instance
     * @return the start ZonedDateTime instance
     */
    public ZonedDateTime getRateZoneStartTm(LocalDate theDate) {
        return ZonedDateTime.of(theDate, this.times.getStartTm(), this.tz.toZoneId());
    }//end method

    /**
     * Gets the rate zone start data/time using the given {@code LocalDate}
     * 
     * @param theDate the local date to use for returning the ZonedDateTime instance
     * @return the start ZonedDateTime instance
     */
    public ZonedDateTime getRateZoneEndTm(LocalDate theDate) {
        return ZonedDateTime.of(theDate, this.times.getEndTm(), this.tz.toZoneId());
    }//end method

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RateDO [days=");
        builder.append(days);
        builder.append(", times=");
        builder.append(times);
        builder.append(", tz=");
        builder.append(tz);
        builder.append(", price=");
        builder.append(price);
        builder.append("]");
        return builder.toString();
    }//end method

}//end class
