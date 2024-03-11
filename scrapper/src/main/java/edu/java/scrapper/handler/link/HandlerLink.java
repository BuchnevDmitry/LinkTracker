package edu.java.scrapper.handler.link;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HandlerLink {
    private HandlerLink next;

    public boolean handle(String url) {
        if (next != null) {
            return next.handle(url);
        }
        return false;
    }


    public HandlerLink bind(HandlerLink next) {
        this.next = next;
        return next;
    }
}
