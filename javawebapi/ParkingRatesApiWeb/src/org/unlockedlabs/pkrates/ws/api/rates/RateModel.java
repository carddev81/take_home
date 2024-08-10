package org.unlockedlabs.pkrates.ws.api.rates;

import java.util.List;


/**
 * Rate Model class used for encapsulating the parking {@code rates}.
 * 
 * @author Richard Salas
 * @version 1.0.0
 */
public class RateModel {

    private List<RateDTO> rates;
    
    /**
     * Constructor used to create a RateModel instance.
     */
    public RateModel() {
        
    }//end constructor
    
    /**
     * Constructor used to create a RateModel instance using the given rates.
     * 
     * @param rates the list of {@code RateDTO}s to use for creating a {@code RateModel} instance
     */
    public RateModel(List<RateDTO> rates) {
        this.rates = rates;
    }//end method

    /**
     * @return the rates
     */
    public List<RateDTO> getRates() {
        return rates;
    }//end method

    /**
     * @param rates the rates to set
     */
    public void setRates(List<RateDTO> rates) {
        this.rates = rates;
    }//end method

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RateModel [rates=");
        builder.append(rates);
        builder.append("]");
        return builder.toString();
    }//end method

}//end class
