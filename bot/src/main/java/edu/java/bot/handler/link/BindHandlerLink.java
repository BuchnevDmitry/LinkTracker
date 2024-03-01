package edu.java.bot.handler.link;

import edu.java.bot.service.ScrapperClient;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BindHandlerLink {

    private ScrapperClient scrapperClient;
    private List<HandlerLink> links = new ArrayList<>();

    @Autowired
    public void setScrapperClient(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    private HandlerLink handlerLink;

    public BindHandlerLink() {
        init();
    }

    void init() {
        StackOverflowHandler stackOverflowHandler = new StackOverflowHandler();
        GitHubHandler gitHubHandler = new GitHubHandler();
        links.add(stackOverflowHandler);
        links.add(gitHubHandler);
        handlerLink = stackOverflowHandler;
        handlerLink.bind(gitHubHandler);
    }

    @PostConstruct
    void setScrapperClient() {
        links.forEach(l -> l.setScrapperClient(scrapperClient));
    }

    public HandlerLink binding() {
        return handlerLink;
    }
}
