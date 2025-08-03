package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IncentiveService {
    private static final Logger logger = LoggerFactory.getLogger(IncentiveService.class);
    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    private final RestTemplate restTemplate;
    
    public IncentiveService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    // Call the API

    public float getIncentive(Transaction transaction) {
        try {
            logger.debug("Calling incentive API for transaction: Sender={}, Recipient={}, Amount={}", 
                        transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());

            // Set up HTTP Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);

            ResponseEntity<Incentive> response = restTemplate.postForEntity(
                INCENTIVE_API_URL,
                request, 
                Incentive.class
            );
            
            if (response.getBody() != null) {
                float incentiveAmount = response.getBody().getAmount();
                logger.info("Rceived incentive amount: {} for transaction amount: {}", 
                           incentiveAmount, transaction.getAmount());
                return incentiveAmount;
            } else { 
                logger.warn("Received null response from incentive API");
                return 0.0f;
            }
        } catch (Exception e) {
            logger.error("Error calling incentive API for transaction: " + transaction, e);
            return 0.0f;
        }
    }
}