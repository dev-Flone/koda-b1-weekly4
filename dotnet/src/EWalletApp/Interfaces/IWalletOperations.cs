using EWalletApp.Models;

namespace EWalletApp.Interfaces;

public interface IWalletOperations
{
    void TopUp(Wallet wallet, decimal amount);

    void Transfer(Wallet sender, Wallet receiver, decimal amount);
}