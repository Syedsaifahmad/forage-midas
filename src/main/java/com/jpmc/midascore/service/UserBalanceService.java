package com.jpmc.midascore.service;

import com.jpmc.midascore.component.DatabaseConduit;
import org.springframework.stereotype.Service;

@Service
public class UserBalanceService {

    private final DatabaseConduit databaseConduit;

    public UserBalanceService(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    public float getBalance(long userId) {
        return databaseConduit.getBalanceForUser(userId);
    }
}
