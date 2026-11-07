using System.Globalization;
using EWalletApp.Data;
using EWalletApp.Models;
using EWalletApp.Services;

namespace EWalletApp;

public class Program
{
    private static readonly List<User> _users = DummyData.CreateUser();
    private static readonly WalletService _walletService = new();
    private static readonly TransactionService _transactionService = new();
    private static User? _currentUser;

    // Localization with Currency / C0
    private static readonly CultureInfo _culture = (CultureInfo)CultureInfo.GetCultureInfo("id-ID").Clone();

    // Custom Currency Symbol
    static Program()
    {
        _culture.NumberFormat.CurrencySymbol = "Rp. ";
    }

    // Main Program
    public static void Main(string[] args)
    {
        InitializeTransactions();

        bool isRunning = true;

        while (isRunning)
        {
            DisplayMenu();
            Console.Write("Select Menu: ");
            string choice = Console.ReadLine() ?? "";

            Console.Clear();

            switch (choice)
            {
                case "1":
                    SelectedWallet();
                    break;
                case "2":
                    DisplayWalletInformation();
                    break;
                case "3":
                    TopUp();
                    break;
                case "4":
                    Transfer();
                    break;
                case "5":
                    DisplayTransactionHistory();
                    break;
                case "6":
                    DisplayTransactionSummary();
                    break;
                case "0":
                    isRunning = false;
                    Console.WriteLine("Thankyou for using this App.");
                    break;
                default:
                    Console.WriteLine("Invalid menu selection");
                    break;
            }

            if (isRunning)
            {
                Console.WriteLine("\nPress any key to continue....");
                Console.ReadKey();
                Console.Clear();
            }
        }
    }

    // METHOD
    private static void DisplayMenu()
    {
        Console.WriteLine("========== E-WALLET APP ==========");
        Console.WriteLine($"{_currentUser?.Name ?? "None"}");

        Console.WriteLine("--------------------------------");
        Console.WriteLine("1. Select Wallet");
        Console.WriteLine("2. Wallet Information");
        Console.WriteLine("3. Top Up");
        Console.WriteLine("4. Transfer");
        Console.WriteLine("5. Transaction History");
        Console.WriteLine("6. Transaction Summary");
        Console.WriteLine("0. Exit");
        Console.WriteLine("==================================");
    }

    private static void SelectedWallet()
    {
        Console.WriteLine("========== SELECT WALLET ==========");
        int i = 1;
        foreach (var user in _users)
        {
            Console.WriteLine($"{i}. {user.UserId} {user.Name} | {user.Wallet.WalletNumber}");
            i++;
        }
        Console.Write("\nEnter Wallet Number: ");
        string walletNumber = Console.ReadLine() ?? "";

        var selectedUser = _users.FirstOrDefault(u => u.Wallet.WalletNumber.Equals(walletNumber, StringComparison.OrdinalIgnoreCase));

        if (selectedUser is null)
        {
            Console.WriteLine("Wallet not found.");
            return;
        }

        _currentUser = selectedUser;
        Console.WriteLine($"Successfully selected {_currentUser.Name}'s Wallet.");
        Console.WriteLine("===================================");
    }

    private static void DisplayWalletInformation()
    {
        if (!EnsureWalletSelected())
        {
            return;
        }
        Console.WriteLine("========== WALLET INFORMATION ==========");
        Console.WriteLine($"User Id : {_currentUser!.UserId}");
        Console.WriteLine($"Name : {_currentUser.Name}");
        Console.WriteLine($"Email : {_currentUser.Email}");
        Console.WriteLine($"Wallet Number : {_currentUser.Wallet.WalletNumber}");
        Console.WriteLine($"Balance : {_currentUser.Wallet.Balance.ToString("C0", _culture)}");
    }


    private static void TopUp()
    {
        if (!EnsureWalletSelected())
        {
            return;
        }
        Console.WriteLine("========== TOP UP ==========");
        Console.Write("Enter amount: Rp. ");
        if (!decimal.TryParse(Console.ReadLine(), out decimal amount))
        {
            Console.WriteLine("Invalid amount.");
            return;
        }

        try
        {
            _walletService.TopUp(_currentUser!.Wallet, amount);

            Console.WriteLine("Top Up successfully");
            Console.WriteLine($"Current balance: {_currentUser.Wallet.Balance.ToString("C0", _culture)}");
        }
        catch (Exception e)
        {
            Console.WriteLine($"Top up failed: {e.Message}");
        }
    }

    private static void Transfer()
    {
        if (!EnsureWalletSelected())
        {
            return;
        }
        Console.WriteLine("========== TRANSFER ==========");
        Console.WriteLine("Destination Wallet Number: ");
        string destinationNumber = Console.ReadLine() ?? "";

        var receiver = _users.FirstOrDefault(u => u.Wallet.WalletNumber.Equals(destinationNumber, StringComparison.OrdinalIgnoreCase));

        if (receiver is null)
        {
            Console.WriteLine("Destination wallet not found.");
            return;
        }

        Console.Write("Enter amount: Rp. ");
        if (!decimal.TryParse(Console.ReadLine(), out decimal amoung))
        {
            Console.WriteLine("Invalid amount.");
            return;
        }

        try
        {
            _walletService.Transfer(_currentUser!.Wallet, receiver.Wallet, amoung);
            Console.WriteLine("Transfer successful!");
            Console.WriteLine($"Recipient: {receiver.Name}");

            Console.WriteLine($"Remaining balance: {_currentUser.Wallet.Balance.ToString("C0", _culture)}");
        }
        catch (Exception e)
        {
            Console.WriteLine($"Transfer failed: {e.Message}");
        }
    }

    private static void DisplayTransactionHistory()
    {
        if (!EnsureWalletSelected())
        {
            return;
        }
        Console.WriteLine("========== TRANSACTION HISTORY ==========");
        Console.WriteLine("1. All Transactions");
        Console.WriteLine("2. Income");
        Console.WriteLine("3. Expense");

        Console.Write("\nSelect filter: ");
        string choice = Console.ReadLine() ?? "";

        var transactions = _transactionService.GetHistory(_currentUser!.Wallet);

        transactions = choice switch
        {
            "1" => transactions,
            "2" => [.. transactions.Where(t => t.Type == TransactionType.TOP_UP || t.Type == TransactionType.TRANSFER_IN)],
            "3" => [.. transactions.Where(t => t.Type == TransactionType.TRANSFER_OUT)],
            _ => []
        };

        if (choice is not ("1" or "2" or "3"))
        {
            Console.WriteLine("Invalid filter selection.");
            return;
        }

        if (transactions.Count == 0)
        {
            Console.WriteLine("No transaction found.");
            return;
        }

        foreach (var transaction in transactions)
        {
            Console.WriteLine("-----------------------------------");
            Console.WriteLine($"Id : {transaction.TransactionId}");
            Console.WriteLine($"Type : {transaction.Type}");
            Console.WriteLine($"Amount : {transaction.Amount.ToString("C0", _culture)}");
            Console.WriteLine($"Date : {transaction.CreatedAt: dd MMM yyyy HH:mm:ss}");
            Console.WriteLine($"Description : {transaction.GetDescription()}");
        }
        Console.WriteLine("-----------------------------------");
    }

    private static void DisplayTransactionSummary()
    {
        if (!EnsureWalletSelected())
        {
            return;
        }
        var wallet = _currentUser!.Wallet;

        decimal income = _transactionService.GetTotalIncome(wallet);
        decimal expense = _transactionService.GetTotalExpense(wallet);

        Console.WriteLine("========== TRANSACTION SUMMARY ==========");
        Console.WriteLine($"Wallet : {wallet.WalletNumber}");
        Console.WriteLine($"Total Income : {income.ToString("C0", _culture)}");
        Console.WriteLine($"Total Expense : {expense.ToString("C0", _culture)}");
        Console.WriteLine($"Current Balance : {wallet.Balance.ToString("C0", _culture)}");
    }


    private static bool EnsureWalletSelected()
    {
        if (_currentUser is not null)
        {
            return true;
        }
        Console.WriteLine("Please select a wallet first.");
        return false;
    }
    private static void InitializeTransactions()
    {
        var andi = _users[0].Wallet;
        var budi = _users[1].Wallet;
        var citra = _users[2].Wallet;

        _walletService.TopUp(andi, 500000m);
        _walletService.TopUp(budi, 300000m);
        _walletService.TopUp(citra, 250000m);

        _walletService.Transfer(andi, budi, 100000m);
        _walletService.Transfer(citra, andi, 50000m);
        _walletService.Transfer(budi, citra, 25000m);
    }
}