package edu.java.bot;

import com.giffing.bucket4j.spring.boot.starter.config.condition.ConditionalOnBucket4jEnabled;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.kafka.KafkaConsumerProperties;
import edu.java.bot.configuration.kafka.KafkaProducerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, KafkaConsumerProperties.class, KafkaProducerProperties.class})
@EnableCaching
@ConditionalOnBucket4jEnabled
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
