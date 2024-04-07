package edu.java.bot.kafka;

import edu.java.bot.model.request.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LinkUpdateListener {

    private final LinkUpdateService linkUpdateService;

    private final DeadLetterQueueProducer dlqProducer;

    @KafkaListener(topics = "${app.scrapper-topic.name}", containerFactory = "linkUpdateContainerFactory")
    public void handleMessage(@Payload @Valid LinkUpdateRequest linkUpdateRequest) {
        try {
            linkUpdateService.update(linkUpdateRequest);
        } catch (Exception e) {
            log.info("Error handleMessage with KafkaListener: " + e.getMessage());
            dlqProducer.sendMessage(linkUpdateRequest);
        }
    }
}
