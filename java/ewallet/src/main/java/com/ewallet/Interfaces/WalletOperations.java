package com.ewallet.Interfaces;

import java.math.BigDecimal;

import com.ewallet.models.User;

public interface WalletOperations {
    void topUp(User user, BigDecimal amount);

    void transfer(User sender, String receiverWalletNumber, BigDecimal amount);
}
