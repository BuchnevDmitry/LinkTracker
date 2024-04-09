package edu.java.scrapper.api.exception;

public class InternalServerErrorException extends CustomException {
    public InternalServerErrorException(String message) {
        super(message, "500");
    }
}
