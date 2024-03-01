package edu.java.bot.handler.link;

import org.springframework.stereotype.Component;

@Component
public class BindHandlerLink {
    private HandlerLink handlerLink;

    public BindHandlerLink() {
        init();
    }

    void init() {
        handlerLink = new StackOverflowHandler();
        handlerLink.bind(new GitHubHandler());
    }

    public HandlerLink binding() {
        return handlerLink;
    }
}
