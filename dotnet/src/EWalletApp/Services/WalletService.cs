using EWalletApp.Interfaces;
using EWalletApp.Models;

namespace EWalletApp.Services;

public class WalletService : IWalletOperations
{
    public void TopUp(Wallet wallet, decimal amount)
    {
        ArgumentNullException.ThrowIfNull(wallet);

        if (amount <= 0)
        {
            throw new ArgumentException("Top up acmount must be greater than 0.");
        }

        var transaction = new TopUpTransaction(Guid.NewGuid().ToString("N"), amount);

        wallet.Deposit(amount);
        wallet.AddTransaction(transaction);
    }

    public void Transfer(Wallet sender, Wallet receiver, decimal amount)
    {
        ArgumentNullException.ThrowIfNull(sender);
        ArgumentNullException.ThrowIfNull(receiver);

        if (amount <= 0)
        {
            throw new ArgumentException("Transfer amount must be greater than 0.");
        }

        if (amount > sender.Balance)
        {
            throw new InvalidOperationException("Insufficient balance.");
        }

        if (sender.WalletNumber == receiver.WalletNumber || ReferenceEquals(sender, receiver))
        {
            throw new InvalidOperationException("Cannot transfer to your own wallet.");
        }

        var transactionId = Guid.NewGuid().ToString();

        var tranferOut = new TransferTransaction(transactionId, amount, sender.WalletNumber, receiver.WalletNumber, TransactionType.TRANSFER_OUT);

        var transferIn = new TransferTransaction(transactionId, amount, sender.WalletNumber, receiver.WalletNumber, TransactionType.TRANSFER_IN);

        sender.Withdraw(amount);
        receiver.Deposit(amount);

        sender.AddTransaction(tranferOut);
        receiver.AddTransaction(transferIn);
    }
}