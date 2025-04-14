package com.rh.poc.common;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.spring.remote.provider.SpringRemoteCacheManager;
import org.infinispan.spring.remote.session.configuration.EnableInfinispanRemoteHttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;

import com.rh.poc.db.User;
import com.rh.poc.db.UserSchemaInitializer;

@Configuration
@EnableSpringHttpSession
@EnableInfinispanRemoteHttpSession(cacheName = "sessions")
public class InfinispanRemoteConfig {

    @Bean
    public RemoteCacheManager remoteCacheManager() {
        return new RemoteCacheManager(
                new ConfigurationBuilder()
                        .addServers("epf-datagrid-poc.datagrid.svc.cluster.local:11222")
                        .security().authentication().username("developer").password("tYxtberWYosXk9aY")
                        .addContextInitializer(new UserSchemaInitializer())
                        .build());
    }
    
    @Bean
    public SpringRemoteCacheManager springRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        return new SpringRemoteCacheManager(remoteCacheManager);
    }
    
    @Bean
    public RemoteCache<Integer, User> userCache(RemoteCacheManager cacheManager) {
        return cacheManager.getCache("users");  // Cache name should match remote cache
    }
    
    @Bean
    public RemoteCache<String, byte[]> imageCache(RemoteCacheManager cacheManager) {
        return cacheManager.getCache("images");  // Cache name should match remote cache
    }
}
