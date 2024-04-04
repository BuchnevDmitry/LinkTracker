package edu.java.bot.kafka;

import edu.java.bot.model.request.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateService;
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

    @KafkaListener(topics = "scrapper.message", containerFactory = "linkUpdateContainerFactory")
    public void handleMessage(@Payload LinkUpdateRequest linkUpdateRequest) {
        linkUpdateService.update(linkUpdateRequest);
    }
}
