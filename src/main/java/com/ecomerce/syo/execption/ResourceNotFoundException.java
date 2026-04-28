package com.ecomerce.syo.execption;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un recurso
 * (por ejemplo: un producto con un ID que no existe)
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    public ResourceNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}