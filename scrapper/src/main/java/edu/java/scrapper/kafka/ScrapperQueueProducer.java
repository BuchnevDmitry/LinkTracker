package edu.java.scrapper.kafka;

import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.service.Updater;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements Updater {

    private final KafkaTemplate<String, LinkUpdateRequest> linkUpdateProducer;

    @Override
    public void sendLinkUpdate(LinkUpdateRequest update) {
        linkUpdateProducer.send("scrapper.message", update);
    }
}
