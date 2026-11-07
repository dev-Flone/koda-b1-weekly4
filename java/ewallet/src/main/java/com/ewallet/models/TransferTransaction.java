package com.ewallet.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferTransaction extends Transaction {
    private TransactionType type;
    private String counterpartyWalletNumber;

    public TransferTransaction(String transactionId, BigDecimal amount, LocalDateTime date, String description,
            TransactionType type, String counterpartyWalletNumber) {
        super(transactionId, amount, date, description);

        if (type != TransactionType.TRANSFER_IN && type != TransactionType.TRANSFER_OUT) {
            throw new IllegalArgumentException("Invalid transfer transaction type.");
        }

        this.type = type;
        this.counterpartyWalletNumber = counterpartyWalletNumber;
    }

    @Override
    public TransactionType getType() {
        return type;
    }

    public String getCounterpartyWalletNumber() {
        return counterpartyWalletNumber;
    }
}
