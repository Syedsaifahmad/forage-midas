package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    @Autowired
    private DatabaseConduit databaseConduit;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-group")
    public void listen(Transaction transaction) {
        boolean success = databaseConduit.processTransaction(transaction);
        if (success) {
            System.out.println("✅ Recorded transaction: " + transaction);
        } else {
            System.out.println("❌ Invalid transaction: " + transaction);
        }
    }
}
