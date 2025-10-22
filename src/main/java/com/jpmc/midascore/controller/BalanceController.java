package com.jpmc.midascore.controller;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.service.UserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    @Autowired
    private UserBalanceService userBalanceService;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") Long userId) {
        float balance = userBalanceService.getBalance(userId);
        return new Balance(balance);
    }

}

