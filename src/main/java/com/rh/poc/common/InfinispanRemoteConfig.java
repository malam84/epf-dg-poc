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
        // Configure the Remote Cache Manager
	System.out.println(">>>>>>>>host>>>>>>>>>>" +host);
    	System.out.println(">>>>>>>>ssl_port>>>>>>>>>>" +ssl_port);
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer()
               .host("epf-datagrid-poc.datagrid.svc.cluster.local") // Remote Infinispan server host
               .port(11222)       // Remote Infinispan server port
               .security()
               .authentication()
               .username("developer") // Username
               .password("tYxtberWYosXk9aY") // Password
               .saslMechanism("DIGEST-MD5")
               .addContextInitializer(new UserSchemaInitializer()); ; // SASL mechanism
        
        return new RemoteCacheManager(builder.build());
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
