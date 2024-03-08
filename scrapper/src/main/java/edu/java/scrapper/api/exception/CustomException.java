package edu.java.scrapper.api.exception;

import edu.java.scrapper.model.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;

abstract class CustomException extends RuntimeException {

    private final String code;

    CustomException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ApiErrorResponse toApiErrorResponse() {
        List<String> stacktrace = Arrays.stream(this.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();

        return new ApiErrorResponse(
            this.getMessage(),
            this.code,
            this.getClass().getName(),
            this.getMessage(),
            stacktrace
        );
    }
}
