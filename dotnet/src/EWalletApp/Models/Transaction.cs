using System;

namespace EWalletApp.Models;

public abstract class Transaction
{
    public string TransactionId { get; }
    public decimal Amount { get; }
    public DateTime CreatedAt { get; }
    public TransactionType Type { get; }

    protected Transaction(string transactionId, decimal amount, TransactionType type)
    {
        if (amount <= 0)
        {
            throw new ArgumentException("Transaction amount must be greater than 0.");
        }
        TransactionId = transactionId;
        Amount = amount;
        Type = type;
        CreatedAt = DateTime.Now;
    }

    public abstract string GetDescription();

}