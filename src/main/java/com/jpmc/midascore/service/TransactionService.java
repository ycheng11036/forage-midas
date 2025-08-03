package com.jpmc.midascore.service;


import com.jpmc.midascore.service.IncentiveService;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class TransactionService { 

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveService incentiveService;

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository, IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        logger.info("Processing transaction - Sender: {}, Recipient: {}, Amount: {}", 
                   transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());

        try { 
            UserRecord sender = userRepository.findById(transaction.getSenderId());
            if (sender == null) {
                logger.warn("Invalid sender ID: {}", transaction.getSenderId());
                return false;
            }

            UserRecord recipient = userRepository.findById(transaction.getRecipientId());
            if (recipient == null) {
                logger.warn("Invalid recipient ID: {}", transaction.getRecipientId());
                return false;
            }

            if (sender.getBalance() < transaction.getAmount()) {
                logger.warn("Insufficient balance for sender {}. Required: {}, Available: {}", 
                       sender.getName(), transaction.getAmount(), sender.getBalance());
                return false;
            }

            // get incentive 
            float incentiveAmount = incentiveService.getIncentive(transaction);
            logger.info("Incentive amount received: {}", incentiveAmount);


            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            userRepository.save(sender);
            userRepository.save(recipient);

            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
            transactionRepository.save(record);

            logger.info("New balance for {}: {}", sender.getName(), sender.getBalance());
            logger.info("New balance for {}: {}", recipient.getName(), recipient.getBalance());

            logger.info("Transaction processed successfully from {} => {}", sender.getName(), recipient.getName());
            return true;

        } catch (Exception e) {
            logger.error("Error processing transaction", e);
            throw e;
        }
    }
}

