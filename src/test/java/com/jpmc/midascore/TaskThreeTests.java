package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {

    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    @Test
    void task_three_verifier() throws InterruptedException {
        // 1️⃣ Populate initial users
        userPopulator.populate();

        // 2️⃣ Send all transactions via Kafka
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // 3️⃣ Wait for transactions to process
        Thread.sleep(2000);

        // 4️⃣ Get Waldorf's balance
        Optional<UserRecord> waldorfOpt = databaseConduit.getUserRepository().findByName("waldorf");
        if (waldorfOpt.isPresent()) {
            float balance = waldorfOpt.get().getBalance();
            int roundedBalance = (int) Math.floor(balance);
            System.out.println("Waldorf's balance: " + roundedBalance);
        } else {
            System.out.println("Waldorf user not found!");
        }

        logger.info("----------------------------------------------------------");
        logger.info("use your debugger to find out what waldorf's balance is after all transactions are processed");

        // 5️⃣ Keep the test running so you can inspect the logs/debugger
        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}
