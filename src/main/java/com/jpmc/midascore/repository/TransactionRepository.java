package com.jpmc.midascore.repository;

import java.util.List;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionRecord, Long> {

    // Find by transaction id
    TransactionRecord findById(long id);

    // Find by sender
    List<TransactionRecord> findBySender(UserRecord sender);

    // Find by recipient
    List<TransactionRecord> findByRecipient(UserRecord recipient);
    
}