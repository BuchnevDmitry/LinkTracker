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

    @KafkaListener(topics = "${app.scrapper-topic.name}", containerFactory = "linkUpdateContainerFactory")
    public void handleMessage(@Payload @Valid LinkUpdateRequest linkUpdateRequest) {
        linkUpdateService.update(linkUpdateRequest);
    }
}
