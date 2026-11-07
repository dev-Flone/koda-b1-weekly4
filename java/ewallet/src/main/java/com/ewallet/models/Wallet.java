package com.ewallet.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wallet {
    private String walletNumber;
    private BigDecimal balance;
    private List<Transaction> transactions;

    public Wallet(String walletNumber, BigDecimal balance) {
        this.walletNumber = walletNumber;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    // METHOD
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        balance = balance.subtract(amount);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalStateException("Transaction cannot be null.");
        }

        transactions.add(transaction);
    }
}
