package edu.java.bot.kafka;

import edu.java.bot.model.request.LinkUpdateRequest;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@Slf4j
public class KafkaConsumerTest {

    public static final String TOPIC_NAME_SEND_CLIENT_DLQ = "scrapper.message_dlq";
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
        registry.add("kafka.consumer.bootstrapServers", kafka::getBootstrapServers);
        registry.add("kafka.producer.bootstrapServers", kafka::getBootstrapServers);
    }

    @Test
    public void sendMessageWithNotValidBodyTest() throws Exception {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-java-test");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class);
        KafkaConsumer<String, LinkUpdateRequest> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(TOPIC_NAME_SEND_CLIENT_DLQ));
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class
        );
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate =
            new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
        LinkUpdateRequest update = new LinkUpdateRequest(1L, new URI("url"), "desc", null);
        Thread.sleep(5000);
        kafkaTemplate.send(TOPIC_NAME_SEND_CLIENT, update);
        Thread.sleep(5000);


        ConsumerRecords<String, LinkUpdateRequest> records = consumer.poll(Duration.ofMillis(10000L));
        consumer.close();

        assertEquals(1, records.count());

    }
}
