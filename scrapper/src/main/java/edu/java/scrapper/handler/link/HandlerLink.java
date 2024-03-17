package edu.java.scrapper.handler.link;

import edu.java.scrapper.api.exception.BadRequestException;
import edu.java.scrapper.model.HandlerData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HandlerLink {
    private HandlerLink next;

    public HandlerData handle(String url) {
        if (next != null) {
            return next.handle(url);
        }
        throw new BadRequestException("Данную ссылку невозможно обработать");
    }


    public HandlerLink bind(HandlerLink next) {
        this.next = next;
        return next;
    }
}
