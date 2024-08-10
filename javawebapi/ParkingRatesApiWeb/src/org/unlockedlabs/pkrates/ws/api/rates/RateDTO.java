package org.unlockedlabs.pkrates.ws.api.rates;

import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.unlockedlabs.pkrates.ws.api.core.IDO;

/**
 * Data transfer object used for holding {@link RateDO} business state.
 * 
 * @author Richard Salas
 * @version 1.0.0
 */
public class RateDTO implements IDO<RateDO>{

    private String days;
    private String times;
    private String tz;
    private int price;

    /**
     * Default constructor
     */
    public RateDTO() { }//end constructor

    /**
     * @return the days
     */
    public String getDays() {
        return days;
    }//end method//end method

    /**
     * @param days the days to set
     */
    public void setDays(String days) {
        this.days = days;
    }//end method

    /**
     * @return the times
     */
    public String getTimes() {
        return times;
    }//end method

    /**
     * @param times the times to set
     */
    public void setTimes(String times) {
        this.times = times;
    }//end method

    /**
     * @return the tz
     */
    public String getTz() {
        return tz;
    }//end method

    /**
     * @param tz the tz to set
     */
    public void setTz(String tz) {
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

    @Override
    public RateDO toDO() {
        RateDO rateDO = new RateDO();
        
        List<Day> days = Stream.of(this.days.split(",")).map(s -> Day.valueOf(s.trim().toUpperCase())).collect(Collectors.toList());
        rateDO.setDays(days);
        
        List<String> times = Stream.of(this.times.split("-")).map(time -> time.trim()).collect(Collectors.toList());
        TimeRange range = new TimeRange(times.get(0), times.get(1));
        rateDO.setTimes(range);
        rateDO.setPrice(this.price);
        rateDO.setTz(TimeZone.getTimeZone(this.getTz()));

        return rateDO;
    }//end method

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RateDTO [days=");
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
