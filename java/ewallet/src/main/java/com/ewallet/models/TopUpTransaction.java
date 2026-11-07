package com.ewallet.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TopUpTransaction extends Transaction {
    public TopUpTransaction(String transactionId, BigDecimal amount, LocalDateTime date,
            String description) {
        super(transactionId, amount, date, description);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.TOP_UP;
    }
}
