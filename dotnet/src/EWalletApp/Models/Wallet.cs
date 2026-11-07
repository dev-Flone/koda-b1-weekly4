using System.Collections;
using System.Transactions;

namespace EWalletApp.Models;

public class Wallet
{
    private readonly List<Transaction> _transactions = [];

    public string WalletNumber { get; }
    public decimal Balance { get; private set; }
    public IReadOnlyList<Transaction> Transactions => _transactions.AsReadOnly();

    public Wallet(string walletNumber, decimal initialBalance)
    {
        if (initialBalance < 0)
        {
            throw new ArgumentException("Initial balance cannot be negative.");
        }

        WalletNumber = walletNumber;
        Balance = initialBalance;
    }

    // METHOD
    public void Deposit(decimal amount)
    {
        if (amount <= 0)
        {
            throw new ArgumentException("Depostit amount must be greater than 0.");
        }
        Balance += amount;
    }

    public void Withdraw(decimal amount)
    {
        if (amount <= 0)
        {
            throw new ArgumentException("Withdraw amount must be greater than 0.");
        }

        if (amount > Balance)
        {
            throw new ArgumentException("Insufficient balance.");
        }

        Balance -= amount;
    }

    public void AddTransaction(Transaction transaction)
    {
        ArgumentNullException.ThrowIfNull(transaction);
        _transactions.Add(transaction);
    }
}
