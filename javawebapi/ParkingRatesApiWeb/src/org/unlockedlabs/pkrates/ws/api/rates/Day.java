/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.rates;

import java.time.DayOfWeek;

/**
 * Day enum contains all abbreviation for days of the week.
 * 
 * @author Richard
 * @version 1.0.0
 */
public enum Day {

    MON,TUES,WED,THURS,FRI,SAT,SUN;

    /**
     * Gets the abbreviation {@code Day} enum based upon the given {@code dayOfWeek}.
     * @param dayOfWeek the full name of the day
     * 
     * @return the {@code Day} abbreviation of the week
     */
    static Day getAbbrevEnum(DayOfWeek dayOfWeek) {
        Day abbreviation = null;
        switch (dayOfWeek) {
        case MONDAY:
            abbreviation = MON;
            break;
        case TUESDAY:
            abbreviation = TUES;
            break;
        case WEDNESDAY:
            abbreviation = WED;
            break;
        case THURSDAY:
            abbreviation = THURS;
            break;
        case FRIDAY:
            abbreviation = FRI;
            break;
        case SATURDAY:
            abbreviation = SAT;
            break;
        case SUNDAY:
        default:
            abbreviation = SUN;
            break;
        }//end switch
        return abbreviation;
    }//end method

}//end enum
