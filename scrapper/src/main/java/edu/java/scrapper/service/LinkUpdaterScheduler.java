package edu.java.scrapper.service;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.model.request.LinkUpdateRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class LinkUpdaterScheduler implements LinkUpdater {

    private final LinkService linkService;

    private final HandlerLinkFacade handlerLinkFacade;

    private final BotClient botClient;

    public LinkUpdaterScheduler(LinkService linkService, HandlerLinkFacade handlerLinkFacade, BotClient botClient) {
        this.linkService = linkService;
        this.handlerLinkFacade = handlerLinkFacade;
        this.botClient = botClient;
    }

    @Override
    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        log.info("Ищем обновление!");
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime criteria = now.minusMinutes(1);
        List<Link> linkList = linkService.findAll(criteria);
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
