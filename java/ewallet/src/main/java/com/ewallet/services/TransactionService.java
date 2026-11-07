package com.ewallet.services;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import com.ewallet.models.Transaction;
import com.ewallet.models.TransactionType;
import com.ewallet.models.User;

public class TransactionService {
    public List<Transaction> getTransactionsByType(User user, TransactionType type) {
        return user.getWallet().getTransactions().stream().filter(transaction -> transaction.getType() == type)
                .toList();
    }

    public List<Transaction> getIncomeTransactions(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(transaction -> transaction.getType() == TransactionType.TOP_UP
                        || transaction.getType() == TransactionType.TRANSFER_IN)
                .toList();
    }

    public List<Transaction> getExpenseTransactions(User user) {
        return getTransactionsByType(user, TransactionType.TRANSFER_OUT);
    }

    public List<Transaction> getSortedTransactions(User user) {
        return user.getWallet().getTransactions().stream().sorted(Comparator.comparing(Transaction::getDate).reversed())
                .toList();
    }

    public BigDecimal getTotalIncome(User user) {
        return getIncomeTransactions(user).stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }

    public BigDecimal getTotalExpense(User user) {
        return getExpenseTransactions(user).stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }
}
