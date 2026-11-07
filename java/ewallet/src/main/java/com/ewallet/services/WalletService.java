package com.ewallet.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ewallet.Interfaces.WalletOperations;
import com.ewallet.models.TopUpTransaction;
import com.ewallet.models.TransactionType;
import com.ewallet.models.TransferTransaction;
import com.ewallet.models.User;
import com.ewallet.models.Wallet;

public class WalletService implements WalletOperations {
    private final List<User> users;

    public WalletService(List<User> users) {
        this.users = users;
    }

    @Override
    public void topUp(User user, BigDecimal amount) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        validateAmount(amount);

        Wallet wallet = user.getWallet();

        TopUpTransaction transaction = new TopUpTransaction(UUID.randomUUID().toString(), amount, LocalDateTime.now(),
                "Wallet top tup");

        wallet.deposit(amount);
        ;
        wallet.addTransaction(transaction);
    }

    @Override
    public void transfer(User sender, String receiverWalletNumber, BigDecimal amount) {
        if (sender == null) {
            throw new IllegalArgumentException("Sender cannot be null.");
        }

        if (receiverWalletNumber == null) {
            throw new IllegalArgumentException("Receiver wallet number is required.");
        }

        validateAmount(amount);

        Wallet senderWallet = sender.getWallet();
        User receiver = users.stream().filter(user -> user.getWallet().getWalletNumber().equals(receiverWalletNumber))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Receiver wallet not found."));

        Wallet receiverWallet = receiver.getWallet();

        if (senderWallet.getWalletNumber().equals(receiverWalletNumber)) {
            throw new IllegalArgumentException("You cannot transfer to your own wallet.");
        }

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient Balance.");
        }

        String transactionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        TransferTransaction outgoing = new TransferTransaction(transactionId, amount, now,
                "Transfer to " + receiver.getName(), TransactionType.TRANSFER_OUT, receiverWallet.getWalletNumber());

        TransferTransaction incoming = new TransferTransaction(transactionId, amount, now,
                "Transfer from " + sender.getName(), TransactionType.TRANSFER_IN, senderWallet.getWalletNumber());

        senderWallet.addTransaction(outgoing);
        receiverWallet.addTransaction(incoming);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalCallerException("Amount must be greater than 0.");
        }
    }
}
