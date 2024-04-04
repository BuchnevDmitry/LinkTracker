package edu.java.scrapper.kafka;

import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.service.Updater;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Primary
@Component
public class ScrapperQueueProducer implements Updater {

    private final KafkaTemplate<String, LinkUpdateRequest> linkUpdateProducer;
    @Override
    public void sendLinkUpdate(LinkUpdateRequest update) {
        linkUpdateProducer.send("scrapper.message", update);
    }
}
