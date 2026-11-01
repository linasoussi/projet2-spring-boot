package com.esb.projet2.exception;

/** Levée quand une ressource n'existe pas -> HTTP 404 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
