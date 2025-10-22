package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // for API calls

    public DatabaseConduit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Save user (for UserPopulator)
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public Optional<UserRecord> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public float getBalanceForUser(long userId) {
        return userRepository.findById(userId)
                .map(UserRecord::getBalance)
                .orElse(0f); // if user not found, return 0
    }



    public boolean processTransaction(Transaction transaction) {
        Optional<UserRecord> optionalSender = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> optionalRecipient = userRepository.findById(transaction.getRecipientId());

        if (optionalSender.isEmpty() || optionalRecipient.isEmpty()) {
            return false; // sender or recipient not found
        }

        UserRecord sender = optionalSender.get();
        UserRecord recipient = optionalRecipient.get();

        // Check sender balance
        if (sender.getBalance() < transaction.getAmount()) {
            return false; // insufficient balance
        }

        // Call Incentive API
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );
        float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0f;

        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        // Save updated users
        userRepository.save(sender);
        userRepository.save(recipient);

        return true;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
