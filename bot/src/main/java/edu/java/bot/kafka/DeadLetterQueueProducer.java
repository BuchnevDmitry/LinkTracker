package edu.java.bot.kafka;

import edu.java.bot.model.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeadLetterQueueProducer {

    private final KafkaTemplate<String, LinkUpdateRequest> producer;

    @Value("${app.dlq-topic.name}")
    private String dlqTopic;

    public void sendMessage(LinkUpdateRequest linkUpdateRequest) {
        producer.send(dlqTopic, linkUpdateRequest);
    }
}
