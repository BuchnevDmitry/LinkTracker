package edu.java.scrapper.kafka;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.service.Updater;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-kafka-test.yml"})
@DirtiesContext
public class KafkaProducerTest extends IntegrationTest {
    public static final String TOPIC_NAME_SEND_CLIENT = "scrapper.message";

    @Container
    public static KafkaContainer kafka =
        new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));

    @BeforeAll
    static void startKafkaContainer() {
        kafka.start();
    }

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("kafka.bootstrapServers", kafka::getBootstrapServers);
    }

    @Autowired
    private Updater producer;


    @Test
    public void sendMessageWithValidBodyTest() throws Exception {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-java-test");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class);
        KafkaConsumer<String, LinkUpdateRequest> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(TOPIC_NAME_SEND_CLIENT));

        LinkUpdateRequest update = new LinkUpdateRequest(1L, new URI("url"), "desc", List.of(1L));
        Thread.sleep(5000);
        producer.sendLinkUpdate(update);
        Thread.sleep(5000);

        ConsumerRecords<String, LinkUpdateRequest> records = consumer.poll(Duration.ofMillis(10000L));
        consumer.close();

        assertEquals(1, records.count());
    }
}
