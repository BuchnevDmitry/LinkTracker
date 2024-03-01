package edu.java.bot.api.exception;

public class ResponseException extends RuntimeException {
    public ResponseException() {
    }

    public ResponseException(String message) {
        super(message);
    }
}
