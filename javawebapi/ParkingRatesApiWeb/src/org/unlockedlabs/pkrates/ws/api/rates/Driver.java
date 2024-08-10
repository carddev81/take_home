/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.rates;

import java.time.DayOfWeek;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.unlockedlabs.pkrates.ws.api.core.RateUnavailableException;

/**
 * 
 */
public class Driver {

    /**
     * 
     */
    public Driver() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     * @throws RateUnavailableException 
     */
    public static void main(String[] args) throws RateUnavailableException {
        //LocalDateTime ldt = LocalDateTime.now(ZoneId.of("America/Chicago"));
        //System.out.println(ldt);
        //2015-07-01T07:00:00-05:00
        //1) check if date passed in spans multiple days if so unavailable! (error)
//        Duration d = Duration.between( startDt , endDt );
//        System.out.println(d.isZero());//if true then doesn't span multiple days
        String start = "2015-07-01T07:00:00-05:00";
        String end = "2015-07-01T12:00:00-05:00";
        DateTimeFormatter dtf = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime startDt = ZonedDateTime.parse(start, dtf);
        ZonedDateTime endDt = ZonedDateTime.parse(end, dtf);

        Period m = Period.between(startDt.toLocalDate(),endDt.toLocalDate());
        System.out.println(m.getDays());

        
        //2)Get RateDO list based by day!
        Day day = Day.getAbbrevEnum(startDt.getDayOfWeek());
        List<RateDO> parkingRatesByDay = getParkingRatesByDay(day);
        ParkingPrice price = ParkingPrice.calculatePrice(startDt, endDt, parkingRatesByDay);
        System.out.println(price);
        //List<RateDO> doList = dao.getParkingRates();
        //Another route is the Duration and Period classes. Use the first for shorter spans of time (hours, minutes, seconds), the second for longer (years, months, days).
//        System.out.println(Day.getAbbrevEnum(startDt.getDayOfWeek()));
//        System.out.println(endDt.getDayOfWeek());
//        startDt.getDayOfWeek();
//        endDt.getDayOfWeek();

//        System.out.println(ldt2.getZone());
                          //2024-08-10T07:47:17.931
//        TimeZone tz = TimeZone.getTimeZone("America/Chicago");
//        
//        System.out.println(tz.toZoneId().toString());

    }

    private static List<RateDO> getParkingRatesByDay(Day day) {

        ParkingRateMemDAO dao = new ParkingRateMemDAO();
        List<RateDO> allRates = dao.getParkingRates();
        List<RateDO> ratesByDayList = allRates.stream().filter(rate -> rate.getDays().contains(day)).collect(Collectors.toList());

        return ratesByDayList;
    }//end method

}//end class
