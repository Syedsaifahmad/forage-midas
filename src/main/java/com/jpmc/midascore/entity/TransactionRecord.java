package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long recipientId;
    private Float amount;

    public TransactionRecord() {}

    public TransactionRecord(Long senderId, Long recipientId, Float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public Long getSenderId() { return senderId; }
    public Long getRecipientId() { return recipientId; }
    public Float getAmount() { return amount; }
}
