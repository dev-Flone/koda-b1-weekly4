package com.ewallet.data;

import com.ewallet.models.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class DummyData {
    public static List<User> createUser() {
        List<User> users = new ArrayList<>();

        Wallet wallet1 = new Wallet("WLT001", BigDecimal.ZERO);
        Wallet wallet2 = new Wallet("WLT002", BigDecimal.ZERO);
        Wallet wallet3 = new Wallet("WLT003", BigDecimal.ZERO);

        User user1 = new User("U001", "Andi", "andi@gmail.com", wallet1);
        User user2 = new User("U002", "Budi", "budi@gmail.com", wallet2);
        User user3 = new User("U003", "Citra", "citra@gmail.com", wallet3);

        users.add(user1);
        users.add(user2);
        users.add(user3);

        addTopUp(user1, 500000L, "Initial top up", 5);
        addTopUp(user2, 300000L, "Initial top up", 4);
        addTopUp(user3, 750000L, "Initial top up", 3);

        addTransfer(user1, user2, 100000L, 2);
        addTransfer(user3, user1, 50000L, 1);
        addTransfer(user2, user3, 25000L, 0);

        return users;
    }

    private static void addTopUp(User user, long amount, String description, int daysAgo) {
        BigDecimal value = new BigDecimal(amount);

        Transaction transaction = new TopUpTransaction("TRX-001", value, LocalDateTime.now().minusDays(daysAgo),
                description);

        user.getWallet().deposit(value);
        user.getWallet().addTransaction(transaction);
    }

    private static void addTransfer(User sender, User receiver, long amount, int daysAgo) {
        BigDecimal value = new BigDecimal(amount);
        String transactionId = "TRX-" + sender.getUserId() + "-" + receiver.getUserId();
        LocalDateTime date = LocalDateTime.now().minusDays(daysAgo);

        Transaction outgoing = new TransferTransaction(transactionId + "-OUT", value, date,
                "Transfer to " + receiver.getName(), TransactionType.TRANSFER_OUT,
                receiver.getWallet().getWalletNumber());

        Transaction incoming = new TransferTransaction(transactionId + "-IN", value, date,
                "Transfer from " + sender.getName(), TransactionType.TRANSFER_IN,
                receiver.getWallet().getWalletNumber());

        sender.getWallet().withdraw(value);
        receiver.getWallet().deposit(value);

        sender.getWallet().addTransaction(outgoing);
        receiver.getWallet().addTransaction(incoming);
    }
}
