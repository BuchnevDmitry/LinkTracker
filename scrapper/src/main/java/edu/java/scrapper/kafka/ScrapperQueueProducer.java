package edu.java.scrapper.kafka;

import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.service.Updater;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements Updater {

    private final KafkaTemplate<String, LinkUpdateRequest> linkUpdateProducer;

    @Value("${app.scrapper-topic.name}")
    private String scrapperTopic;

    @Override
    public void sendLinkUpdate(LinkUpdateRequest update) {
        linkUpdateProducer.send(scrapperTopic, update);
    }
}
