package edu.java.bot.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaErrorHandler implements CommonErrorHandler {

    private final KafkaTemplate<Object, Object> dlqProducer;

    @Value("${app.dlq-topic.name}")
    private String dlqTopic;

    @Override
    public void handleOtherException(
        Exception exception,
        Consumer<?, ?> consumer,
        MessageListenerContainer container,
        boolean batchListener
    ) {
        log.error("(HandleOther) Exception thrown " + exception.getClass());
        if (exception instanceof RecordDeserializationException ex) {
            consumer.seek(ex.topicPartition(), ex.offset() + 1L);
            consumer.commitSync();
        } else {
            log.error("Exception not handled " + exception.getClass());
        }
    }

    @Override
    public boolean handleOne(
        Exception exception,
        ConsumerRecord<?, ?> consumerRecord,
        Consumer<?, ?> consumer,
        MessageListenerContainer container
    ) {
        log.error("(HandleOne) Exception thrown -> class: " + exception.getClass());
        consumer.seek(
            new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
            consumerRecord.offset() + 1L
        );
        consumer.commitSync();
        log.error("DLQ Message: " + consumerRecord.value());
        dlqProducer.send(dlqTopic, consumerRecord.value());
        return true;
    }

}
