using EWalletApp.Models;

namespace EWalletApp.Data;

public static class DummyData
{
    public static List<User> CreateUser()
    {
        var users = new List<User>
        {
            new(
                "U001",
                "Andi",
                "andi@email.com",
                new Wallet("WLT001", 0m)
            ),

            new(
                "U002",
                "Budi",
                "budi@email.com",
                new Wallet("WLT002", 0m)
            ),

            new(
                "U003",
                "Citra",
                "citra@email.com",
                new Wallet("WLT003", 0m)
            )
        };
        return users;
    }
}