package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.kafka.annotation.KafkaListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class MidasCoreApplication {

    private final Logger logger = LoggerFactory.getLogger(MidasCoreApplication.class);
    private final TransactionService transactionService;

    public MidasCoreApplication(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MidasCoreApplication.class, args);
    }

    @KafkaListener(id = "listen1", topics = "transactions")
	public void listen(Transaction transaction) {
		logger.info("Received Transaction: " + transaction);

        try { 
            boolean processed = transactionService.processTransaction(transaction);
            
            if (processed) { 
                logger.info("Transaction is successfully processed and recorded");
            } else {
                logger.warn("Transaction is invalid");
            }
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", transaction, e);
        }
	}
}
