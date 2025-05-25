package com.autohubreactive.apigateway.scheduler;

import com.autohubreactive.apigateway.cache.CacheUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheUpdateScheduler {

    private final CacheUpdateService cacheUpdateService;

    @Scheduled(cron = "${cache.swagger-update-frequency}")
    public void updateCache() {
        cacheUpdateService.populateCache().blockLast();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadCacheOnStartup() {
        cacheUpdateService.populateCache().blockLast();
    }

}
