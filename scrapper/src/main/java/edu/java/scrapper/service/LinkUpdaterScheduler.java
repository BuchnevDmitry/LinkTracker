package edu.java.scrapper.service;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.HandlerData;
import edu.java.scrapper.model.LinkStatus;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class LinkUpdaterScheduler {

    private final LinkService linkService;

    private final HandlerLinkFacade handlerLinkFacade;

    private final BotClient botClient;

    public LinkUpdaterScheduler(LinkService linkService, HandlerLinkFacade handlerLinkFacade, BotClient botClient) {
        this.linkService = linkService;
        this.handlerLinkFacade = handlerLinkFacade;
        this.botClient = botClient;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        log.info("Ищем обновление!");
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime criteria = now.minusMinutes(1);
        List<Link> linkList = linkService.findAll(criteria);
        for (Link link : linkList) {
            HandlerData handlerData = handlerLinkFacade.getChainHead().handle(link.getUrl());
            linkService.updateLink(link.getId(), now, handlerData.hash());
            log.info(link.getUrl() + "-> hash: " + handlerData.hash());
            List<Chat> longList = linkService.getChats(link.getId());
            List<Long> chatIds = longList.stream().map(Chat::getId).toList();
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
