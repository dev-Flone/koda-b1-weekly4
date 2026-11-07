using EWalletApp.Models;

namespace EWalletApp.Services;

public class TransactionService
{
    public List<Transaction> GetHistory(Wallet wallet)
    {
        ArgumentNullException.ThrowIfNull(wallet);

        return [.. wallet.Transactions.OrderByDescending(t => t.CreatedAt)];
    }

    public List<Transaction> GetHistoryByType(Wallet wallet, TransactionType type)
    {
        ArgumentNullException.ThrowIfNull(wallet);

        return [.. wallet.Transactions.Where(t => t.Type == type).OrderByDescending(t => t.CreatedAt)];
    }

    public decimal GetTotalIncome(Wallet wallet)
    {
        ArgumentNullException.ThrowIfNull(wallet);

        return wallet.Transactions.Where(t => t.Type == TransactionType.TOP_UP || t.Type == TransactionType.TRANSFER_IN).Sum(t => t.Amount);
    }

    public decimal GetTotalExpense(Wallet wallet)
    {
        ArgumentNullException.ThrowIfNull(wallet);

        return wallet.Transactions.Where(t => t.Type == TransactionType.TRANSFER_OUT).Sum(t => t.Amount);
    }
}