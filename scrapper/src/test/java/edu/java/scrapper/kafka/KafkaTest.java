package edu.java.scrapper.kafka;

import edu.java.scrapper.model.request.LinkUpdateRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaTest {

    @Autowired
    private ScrapperQueueProducer scrapperQueueProducer;

    @Test
    void test() throws URISyntaxException {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(1L, new URI("url"), "adas", List.of(1L, 2L));
        scrapperQueueProducer.sendLinkUpdate(linkUpdateRequest);
    }
}
