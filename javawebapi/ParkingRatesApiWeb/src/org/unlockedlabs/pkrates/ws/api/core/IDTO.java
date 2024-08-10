package org.unlockedlabs.pkrates.ws.api.core;

/**
 * An IDTO is an entity that wraps a Type value for creating a Data Transfer Object based on the implementers state.
 * 
 * @author Richard Salas
 * @version 1.0.0
 *
 * @param <T> The type of the wrapped value.
 */
public interface IDTO<T> {

    /**
     * Creates a data transfer object based on the implementation state.
     * 
     * @return the data transfer object
     */
    public T toDTO();
    
}//end interface
