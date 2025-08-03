package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class BalanceController { 
    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);

    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam long userId) {
        logger.debug("Retrieving balance for user ID: {}", userId);
        
        try { 
            UserRecord user = userRepository.findById(userId);

            if (user != null) {
                float balance = user.getBalance();
                logger.debug("Found user {} with balance: {}", user.getName(), balance);
                return new Balance(balance);
            } else { 
                logger.debug("User with ID {} not found, returning balance of 0", userId);
                return new Balance(0.0f);
            }
        } catch (Exception e) {
                logger.error("Error retrieving balance for user ID: " + userId, e);
                // Return 0 balance in case of any error
                return new Balance(0.0f);
        }
    }
}
