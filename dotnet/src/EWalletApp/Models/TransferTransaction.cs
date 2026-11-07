using System.ComponentModel.DataAnnotations;
using System.Globalization;

namespace EWalletApp.Models;

public class TransferTransaction : Transaction
{
    [Required]
    public string SenderWalletNumber { get; }
    [Required]
    public string ReceiverWalletNumber { get; }

    public TransferTransaction(string transactionId, decimal amount, string senderWalletNumber, string receiverWalletNumber, TransactionType type) : base(transactionId, amount, type)
    {
        if (type != TransactionType.TRANSFER_IN && type != TransactionType.TRANSFER_OUT)
        {
            throw new ArgumentException("Invaid transaction type.");
        }

        SenderWalletNumber = senderWalletNumber;
        ReceiverWalletNumber = receiverWalletNumber;
    }

    public override string GetDescription()
    {
        var culture = CultureInfo.GetCultureInfo("id-ID");
        var formattedAmount = Amount.ToString("C0", culture);

        return Type switch
        {
            TransactionType.TRANSFER_OUT => $"Transfer Rp. {formattedAmount} to {ReceiverWalletNumber}",
            TransactionType.TRANSFER_IN => $"Transfer Rp. {formattedAmount} from {SenderWalletNumber}",
            _ => "Unknown transaction"
        };
    }
}