package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue()
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentive;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    protected TransactionRecord(){}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive){
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
        this.timestamp = LocalDateTime.now();
        
    }

    @Override
    public String toString() { 
               return String.format("TransactionRecord[id=%d, sender='%s', recipient='%s', amount=%.2f, incentive=%.2f, timestamp=%s]", 
                           id, 
                           sender != null ? sender.getName() : "null", 
                           recipient != null ? recipient.getName() : "null", 
                           amount,
                           incentive, 
                           timestamp 
                           );
    }

    public long getId() {
        return id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public float getAmount() {
        return amount;
    }

    public float getIncentive() {
        return incentive;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getSenderId() {
        return sender != null ? sender.getId() : 0;
    }

    public long getRecipientId() {
        return recipient != null ? recipient.getId() : 0;
    }

    
}