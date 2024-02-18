package edu.java.scrapper.service.impl;

import edu.java.scrapper.service.LinkUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@EnableScheduling
@Service
public class LinkUpdaterScheduler implements LinkUpdater {
    @Override
    @Scheduled(fixedDelayString = "#{@schedulerInterval}")
    public void update() {
        log.info("Обновление!");
    }
}
