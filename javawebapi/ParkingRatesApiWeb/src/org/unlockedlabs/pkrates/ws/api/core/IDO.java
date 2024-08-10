package org.unlockedlabs.pkrates.ws.api.core;

/**
 * An IDO is an entity that wraps a Type value for creating a Data Object based on the implementers state.
 * 
 * @author Richard Salas
 * @version 1.0.0
 *
 * @param <T> The type of the wrapped value.
 */
public interface IDO<T> {

    /**
     * Creates a data object based on the implementation state.
     * 
     * @return the data object
     */
    public T toDO();
    
}//end interface
