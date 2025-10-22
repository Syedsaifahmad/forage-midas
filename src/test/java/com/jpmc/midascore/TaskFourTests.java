package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TaskFourTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        Thread.sleep(3000); // wait for transactions to be processed

        // Fetch wilbur's balance
        Optional<UserRecord> wilburOpt = databaseConduit.getUserByName("wilbur");
        if (wilburOpt.isPresent()) {
            float balance = wilburOpt.get().getBalance();
            int roundedBalance = (int) Math.floor(balance);
            System.out.println("Wilbur's balance (rounded down): " + roundedBalance);
        } else {
            System.out.println("Wilbur not found!");
        }
    }
}
