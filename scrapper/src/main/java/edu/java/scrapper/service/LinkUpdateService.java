package edu.java.scrapper.service;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.LinkStatus;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.net.URI;
import java.net.URISyntaxException;
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
            HandlerData handlerData = handlerLinkFacade.getChainHead().handle(link.getUrl());
            linkService.updateLink(link.getId(), now, handlerData.hash());
            log.info(link.getUrl() + "-> hash: " + handlerData.hash());
            List<Chat> chats = linkService.getChats(link.getId());
            List<Long> chatIds = chats.stream().map(Chat::getId).toList();
            if (handlerData.typeUpdate().equals(LinkStatus.UPDATE)) {
                try {
                    botClient.addUpdate(new LinkUpdateRequest(
                        link.getId(),
                        new URI(link.getUrl()),
                        handlerData.description(),
                        chatIds
                    ));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
