package edu.java.scrapper.handler.link;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HandlerLinkFacade {
    private final List<HandlerLink> handlerLinks;

    public HandlerLinkFacade(List<HandlerLink> handlerLinks) {
        this.handlerLinks = handlerLinks;
    }

    @PostConstruct
    void init() {
        for (int i = 0; i < handlerLinks.size() - 1; i++) {
            handlerLinks.get(i).bind(handlerLinks.get(i+1));
        }
    }

    public HandlerLink getChainHead() {
        return handlerLinks.get(0);
    }
}
