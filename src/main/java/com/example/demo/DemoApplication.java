package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import jakarta.persistence.*;        
import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication {
    
    private final UserRepository userRepo;
    private final StringRedisTemplate redis;
    
    public DemoApplication(UserRepository userRepo, StringRedisTemplate redis) {
        this.userRepo = userRepo;
        this.redis = redis;
    }
    
    @GetMapping("/health")
    public String health() {
        return "UP";
    }
    
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepo.findAll();
    }
    
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return userRepo.save(user);
    }
    
    @GetMapping("/cache/{key}")
    public String getCache(@PathVariable String key) {
        String value = redis.opsForValue().get(key);
        if (value == null) {
            value = "cached-value-" + System.currentTimeMillis();
            redis.opsForValue().set(key, value);
        }
        return value;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}