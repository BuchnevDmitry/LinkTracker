package edu.java.scrapper;

import com.giffing.bucket4j.spring.boot.starter.config.condition.ConditionalOnBucket4jEnabled;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.SchedulerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, SchedulerConfig.class})
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableCaching
@ConditionalOnBucket4jEnabled
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
