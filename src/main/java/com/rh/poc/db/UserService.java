package com.rh.poc.db;

import java.util.Optional;

import org.infinispan.client.hotrod.RemoteCache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    
    @Autowired
UserRepository userRepository;
    
@Autowired
    RemoteCache<Integer, User> userCache;
    
    // Read-Through: Fetch from cache, if not found then fetch from DB
    @Cacheable(value = "users-cache", key = "#id")
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userCache.computeIfAbsent(id, key -> 
            userRepository.findById(key).orElse(null)  // Handle Optional properly
        ));
    }

    // Write-Behind: Update cache first, then asynchronously save to DB
    @Transactional
    @CachePut(value = "users-cache", key = "#user.id")
    public User saveUser(User user) {
        userCache.put(user.getId(), user);  // Update cache first
        return userRepository.save(user);   // Save to DB
    }
}
