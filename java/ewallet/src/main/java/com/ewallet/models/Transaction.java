package com.ewallet.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Transaction {
    private String transactionId;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Transaction(String transactionId, BigDecimal amount, LocalDateTime date, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than 0.");
        }

        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public abstract TransactionType getType();
}
