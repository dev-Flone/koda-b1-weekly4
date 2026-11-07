package com.ewallet;

import com.ewallet.data.DummyData;
import com.ewallet.models.Transaction;
import com.ewallet.models.User;
import com.ewallet.models.Wallet;
import com.ewallet.services.TransactionService;
import com.ewallet.services.WalletService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<User> users = DummyData.createUser();

        WalletService walletService = new WalletService(users);
        TransactionService transactionService = new TransactionService();

        boolean running = true;

        while (running) {
            System.out.println("\n========== E-WALLET ==========");
            System.out.println("1. Select Account");
            System.out.println("0. Exit");
            System.out.print("Choose Menu: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> {
                    User selectedUser = selectAccount(users);

                    if (selectedUser != null) {
                        walletMenu(selectedUser, walletService, transactionService);
                    }
                }
                case 0 -> {
                    running = false;
                    System.out.println("Thank you for using E-Wallet!");
                }
                default -> System.out.println("Invalid menu choice");
            }
        }
        scanner.close();
    }

    private static User selectAccount(List<User> users) {
        System.out.println("\n========== SELECT ACCOUNT ==========");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getName());
        }

        System.out.println("0. Back");
        System.out.print("Select account: ");

        int choice = readInt();

        if (choice == 0) {
            return null;
        }

        if (choice < 1 || choice > users.size()) {
            System.out.println("Invalid account selection.");
            return null;
        }

        return users.get(choice - 1);
    }

    private static void walletMenu(User user, WalletService walletService, TransactionService transactionService) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========== WALLET MENU ==========");
            System.out.println("Welcome, " + user.getName() + "!");
            System.out.println("1. View Wallet Information");
            System.out.println("2. Check Balance");
            System.out.println("3. Top Up");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("0. Back");
            System.out.print("Choose menu: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> viewWalletInformation(user);
                case 2 -> checkBalance(user);
                case 3 -> handleTopUp(user, walletService);
                case 4 -> handleTransfer(user, walletService);
                case 5 -> showTransactionHistory(user, transactionService);
                case 0 -> {
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                }
                default -> System.out.println("Invalid menu choice.");
            }
        }
    }

    private static void viewWalletInformation(User user) {
        Wallet wallet = user.getWallet();
        System.out.println("\n========== WALLET INFORMATION ==========");
        System.out.println("User ID : " + user.getUserId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email : " + user.getEmail());
        System.out.println("Wallet Number : " + wallet.getWalletNumber());
        System.out.println("Balance : Rp. " + wallet.getBalance());
    }

    private static void checkBalance(User user) {
        System.out.println("\n========== BALANCE ==========");
        System.out.println("Current balance: Rp. " + user.getWallet().getBalance());
    }

    private static void handleTopUp(User user, WalletService walletService) {
        System.out.println("\n========== TOP UP ==========");
        System.out.print("Enter amount: Rp. ");

        BigDecimal amount = readAccount();

        try {
            walletService.topUp(user, amount);

            System.out.println("Top up successfull");
            System.out.println("Current Balance: Rp. " + user.getWallet().getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Top up failed: " + e.getMessage());
        }
    }

    private static void handleTransfer(User sender, WalletService walletService) {
        System.out.println("\n========== TRANSFER ==========");
        System.out.print("Receiver Wallet Number: ");
        String receiveWalletNumber = scanner.nextLine().trim();

        System.out.print("Enter amount: Rp. ");

        BigDecimal amount = readAccount();

        try {
            walletService.transfer(sender, receiveWalletNumber, amount);

            System.out.println("Transfer successfull!");
            System.out.println("Remaining Balance: Rp. " + sender.getWallet().getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    private static void showTransactionHistory(User user, TransactionService transactionService) {
        System.out.println("\n========== TRANSACTION HISTORY ==========");
        System.out.println("1. All transaction");
        System.out.println("2. Income");
        System.out.println("3. Expense");
        System.out.print("Choose filter: ");

        int choice = readInt();

        List<Transaction> transactions;

        switch (choice) {
            case 1 -> transactions = transactionService.getSortedTransactions(user);
            case 2 -> transactions = transactionService.getIncomeTransactions(user);
            case 3 -> transactions = transactionService.getExpenseTransactions(user);

            default -> {
                System.out.println("No transactions found.");
                return;
            }
        }

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println("------------------------------");
            System.out.println("Type : " + transaction.getType());
            System.out.println("Amount : " + transaction.getAmount());
            System.out.println("Date : " + transaction.getDate());
            System.out.println("Description : " + transaction.getDescription());
        }

        System.out.println("------------------------------");

        System.out.println("Total Income : Rp. " + transactionService.getTotalIncome(user));
        System.out.println("Total Expense : Rp. " + transactionService.getTotalExpense(user));
    }

    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static BigDecimal readAccount() {
        try {
            return new BigDecimal(scanner.nextLine().trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
