package edu.java.bot.handler.link;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StackOverflowHandler extends HandlerLink {
    @Override
    public boolean handle(String url) {
        if (url.startsWith("https://stackoverflow.com/")) {
            return true;
        }
        return super.handle(url);
    }
}
