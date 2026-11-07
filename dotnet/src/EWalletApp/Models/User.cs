namespace EWalletApp.Models;

public class User(string userId, string name, string email, Wallet wallet)
{
    public string UserId { get; } = userId;
    public string Name { get; } = name;
    public string Email { get; } = email;
    public Wallet Wallet { get; } = wallet ?? throw new ArgumentNullException(nameof(wallet));
}