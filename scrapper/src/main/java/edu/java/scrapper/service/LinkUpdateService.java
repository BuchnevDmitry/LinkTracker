package edu.java.scrapper.service;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkUpdateService {

    private final LinkService linkService;

    private final HandlerLinkFacade handlerLinkFacade;

    private final BotClient botClient;

    public void performLinkUpdate() {
        log.info("Ищем обновление!");
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime time = now.minusMinutes(1);
        List<Link> linkList = linkService.findAllByLastCheckTimeBefore(time);
        for (Link link : linkList) {
            Integer hash = handlerLinkFacade.getChainHead().handle(link.url().toString());
            linkService.updateLink(link.id(), now, hash);
            log.info(link.url() + "-> hash: " + hash);
            List<ChatResponse> longList = linkService.getChats(link.id());
            List<Long> chatIds = longList.stream().map(ChatResponse::id).toList();
            if (!link.hashInt().equals(hash)) {
                botClient.addUpdate(new LinkUpdateRequest(
                    link.id(),
                    link.url(),
                    "появилось новое обновление",
                    chatIds
                ));
            }
        }
    }
}
