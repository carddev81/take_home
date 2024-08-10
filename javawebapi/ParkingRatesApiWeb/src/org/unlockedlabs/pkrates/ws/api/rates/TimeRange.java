package org.unlockedlabs.pkrates.ws.api.rates;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The class TimeRange used to encapsulate start time and end time.
 *
 * @author Richard Salas
 */
public class TimeRange implements Serializable {

    private static final long serialVersionUID = -3968359371025068983L;
    private static final DateTimeFormatter HOUR_MIN = DateTimeFormatter.ofPattern("HHmm");
    
    private LocalTime startTm;
    private LocalTime endTm;

    /**
     * Constructor used to create a TimeRange instance.
     * 
     * @param endTm the end time
     * @param startTm the start time
     */
    public TimeRange(String startTm, String endTm) {
        this.startTm = LocalTime.parse(startTm, HOUR_MIN);
        this.endTm = LocalTime.parse(endTm, HOUR_MIN);
    }//end constructor

    /**
     * Constructor used to create a TimeRange instance.
     *
     * @param startTm
     *        the start time
     * @param endTm
     *        the end time
     */
    public TimeRange(LocalTime startTm, LocalTime endTm) {
        this.startTm = startTm;
        this.endTm = endTm;
    }//end constructor

    /**
     * Gets the start tm.
     *
     * @return the startDt
     */
    public LocalTime getStartTm() {
        return this.startTm;
    }//end method

    /**
     * Sets the start tm.
     *
     * @param startDt
     *        the startDt to set
     */
    public void setStartTm(LocalTime startTm) {
        this.startTm = startTm;
    }

    /**
     * Gets the end tm.
     *
     * @return the endDt
     */
    public LocalTime getEndTm() {
        return this.endTm;
    }//end method

    /**
     * Sets the end tm.
     *
     * @param endDt
     *        the endDt to set
     */
    public void setEndTm(LocalTime endTm) {
        this.endTm = endTm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(HOUR_MIN.format(startTm));
        builder.append("-").append(HOUR_MIN.format(endTm));
        return builder.toString();
    }// end method

}// end class
