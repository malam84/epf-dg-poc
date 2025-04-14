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

  /*  @Value("${infinispan.client.hotrod.server}")
    private String host;

    @Value("${infinispan.client.hotrod.port}")
    private int ssl_port;

    @Value("${infinispan.client.hotrod.auth_username}")
    private String userName;

    @Value("${infinispan.client.hotrod.auth_password}")
    private String password;
	
    @Value("${infinispan.client.hotrod.sasl_mechanism}")
    private String saslMechanism;

    @Value("${infinispan.client.hotrod.sni_host_name}")
    private String sniHostName;

    @Value("${infinispan.client.hotrod.trust_store_path}")
    private String trustStorePath; */

    @Bean
    public RemoteCacheManager remoteCacheManager() {
        return new RemoteCacheManager(
                new ConfigurationBuilder()
                        .addServers("epf-datagrid-poc.datagrid.svc.cluster.local:11222")
                        .security().authentication().username("developer").password("tYxtberWYosXk9aY")
                        .clientIntelligence(ClientIntelligence.HASH_DISTRIBUTION_AWARE)
                        .marshaller(new GenericJBossMarshaller())
                        .addJavaSerialWhiteList(".*")
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
