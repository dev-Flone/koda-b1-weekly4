using System.Globalization;

namespace EWalletApp.Models;

public class TopUpTransaction : Transaction
{
    public TopUpTransaction(string transactionId, decimal amount) : base(transactionId, amount, TransactionType.TOP_UP)
    {

    }

    public override string GetDescription()
    {
        var culture = CultureInfo.GetCultureInfo("id-ID");
        return $"Top Up Rp. {Amount.ToString("C0", culture)}";
    }
}