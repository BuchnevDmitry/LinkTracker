package edu.java.bot.client;

import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Component
public class RetryPolicy {

    private static final String DURATION = "duration: ";
    private static final String NOT_EXECUTE = "Не удалось выполнить запрос после повторных попыток";

    public Retry linear(
        Duration initialBackoff,
        int maxAttempts,
        List<Class<? extends Throwable>> retryableExceptions
    ) {
        return Retry.fixedDelay(maxAttempts, initialBackoff)
            .doBeforeRetryAsync(signal -> {
                retryLog(signal.totalRetries());
                return Mono.delay(Duration.ofSeconds(
                    (initialBackoff.getSeconds() * signal.totalRetries()) - initialBackoff.getSeconds())).then();
            })
            .filter(throwable -> isRetryableException(throwable, retryableExceptions))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException(NOT_EXECUTE));

    }

    public Retry constant(
        Duration initialBackoff,
        int maxAttempts,
        List<Class<? extends Throwable>> retryableExceptions
    ) {
        return Retry.fixedDelay(maxAttempts, initialBackoff)
            .doAfterRetry(x -> retryLog(x.totalRetries()))
            .filter(throwable -> isRetryableException(throwable, retryableExceptions))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException(NOT_EXECUTE));
    }

    public Retry exponential(
        Duration initialBackoff,
        int maxAttempts,
        List<Class<? extends Throwable>> retryableExceptions
    ) {
        return Retry.backoff(maxAttempts, initialBackoff)
            .doAfterRetry(x -> retryLog(x.totalRetries()))
            .filter(throwable -> isRetryableException(throwable, retryableExceptions))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RuntimeException(NOT_EXECUTE));
    }

    private void retryLog(long totalRetries) {
        log.info(DURATION + totalRetries);
    }

    private boolean isRetryableException(
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
