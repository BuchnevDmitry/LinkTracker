package edu.java.scrapper.api.exception;

public class ResourceAlreadyExistsException extends CustomException {

    public ResourceAlreadyExistsException(String message) {
        super(message, "409");
    }
}
