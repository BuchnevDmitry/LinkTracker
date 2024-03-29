package edu.java.scrapper.client;

import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class RetryUtil {

    private static final String DURATION = "duration: ";
    private static final String NOT_EXECUTE = "Не удалось выполнить запрос после повторных попыток";

    private RetryUtil() {
    }

    public static Retry linear(
        Duration initialBackoff,
        int maxAttempts,
        List<Class<? extends Throwable>> retryableExceptions
    ) {
        return Retry.fixedDelay(maxAttempts, initialBackoff)
            .doBeforeRetryAsync(signal -> {
                log.info(DURATION + (signal.totalRetries()));
                return Mono.delay(Duration.ofSeconds(
                    (initialBackoff.getSeconds() * signal.totalRetries()) - initialBackoff.getSeconds())).then();
            })
            .filter(throwable -> isRetryableException(throwable, retryableExceptions))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException(NOT_EXECUTE));

    }

    public static Retry constant(
        Duration initialBackoff,
        int maxAttempts,
        List<Class<? extends Throwable>> retryableExceptions
    ) {
        return Retry.fixedDelay(maxAttempts, initialBackoff)
            .doAfterRetry(x -> log.info(DURATION + x.totalRetries()))
            .filter(throwable -> isRetryableException(throwable, retryableExceptions))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException(NOT_EXECUTE));
    }

    public static Retry exponential(
        Duration initialBackoff,
        int maxAttempts,
        List<Class<? extends Throwable>> retryableExceptions
    ) {
        return Retry.backoff(maxAttempts, initialBackoff)
            .doAfterRetry(x -> log.info(DURATION + x.totalRetries()))
            .filter(throwable -> isRetryableException(throwable, retryableExceptions))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException(NOT_EXECUTE));
    }

    private static boolean isRetryableException(
        Throwable throwable,
        List<Class<? extends Throwable>> retryableExceptions
    ) {
        for (Class<? extends Throwable> exceptionClass : retryableExceptions) {
            if (exceptionClass.isInstance(throwable)) {
                return true;
            }
        }
        return false;
    }

}
