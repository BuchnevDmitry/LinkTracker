package edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkUpdateService linkUpdateService;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        linkUpdateService.performLinkUpdate();
    }
}
