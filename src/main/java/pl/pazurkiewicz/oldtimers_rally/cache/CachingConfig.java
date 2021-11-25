package pl.pazurkiewicz.oldtimers_rally.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class CachingConfig {
    @Autowired
    CacheManager cacheManager;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("eventsExists"),
                new ConcurrentMapCache("eventsUrl"),
                new ConcurrentMapCache("languages"),
                new ConcurrentMapCache("userPrivileges")
        ));
        return cacheManager;
    }

    @Scheduled(fixedRate = 600000)
    public void evictAllUserPrivileges() {
        Objects.requireNonNull(cacheManager.getCache("userPrivileges")).clear();
    }

}
