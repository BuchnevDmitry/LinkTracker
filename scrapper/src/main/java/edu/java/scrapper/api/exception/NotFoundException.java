package edu.java.scrapper.api.exception;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message, "404");
    }
}
