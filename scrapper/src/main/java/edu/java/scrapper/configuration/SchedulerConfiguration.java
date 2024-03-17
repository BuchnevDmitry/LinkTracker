package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.handler.link.HandlerLinkFacade;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.LinkUpdaterScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {
    @Bean
    public LinkUpdater linkUpdaterScheduler(
        @Qualifier("jdbcLinkService") LinkService linkService,
        HandlerLinkFacade handlerLinkFacade,
        BotClient botClient
    ) {
        return new LinkUpdaterScheduler(linkService, handlerLinkFacade, botClient);
    }

}
