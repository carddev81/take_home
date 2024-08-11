/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 * Class used for custom param converter for a ZonedDateTime instance being parsed from a user's request.
 * 
 * @author Richard Salas
 * @version 1.0.0
 */
@Provider
public class ZonedDateTimeConverterProvider implements ParamConverterProvider{

    /**
     * Creates a ZonedDateTimeConverterProvider instance
     */
    public ZonedDateTimeConverterProvider() { } //end constructor

    public static final DateTimeFormatter DTF = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    /**
     * Gets a ParamConverter instance that can convert a string into a ZonedDateTime instance using the given rawType, genericType, and annotations.
     * @param rawType the raw type of the object to be converted
     * @param genericType the type of object to be converted 
     * @param annotations an array of the annotations that are linked with the convertible parameter instance 
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType == ZonedDateTime.class) {
            return (ParamConverter<T>) new Java8LocalDateClassicFormatParamConverter();
        }//end if
        return null;
    }//end method

    /**
     * The {@link ParamConverter} for {@link ZonedDateTime} using the {@link DateTimeFormatter#ISO_ZONED_DATE_TIME}.
     */
    private static class Java8LocalDateClassicFormatParamConverter implements ParamConverter<ZonedDateTime> {

        /**
         * Converts the given {@code value} into a ZonedDateTime.
         * 
         * @param value the value to parse
         * @return a ZonedDateTime instance
         */
        @Override
        public ZonedDateTime fromString(String value) {
            return ZonedDateTime.parse(value, DTF);
        }//end method

        /**
         * Converts the given {@code value} into a string.
         * 
         * @param value the value to parse
         * @return a String instance
         */
        @Override
        public String toString(ZonedDateTime value) {
            return DTF.format(value);
        }//end method

    }//end inner class
    
}//end class
