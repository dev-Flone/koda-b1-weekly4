# E-Wallet Console Application

Proyek latihan OOP aplikasi e-wallet berbasis console dengan dua implementasi terpisah: **Java** dan **C#/.NET**. Keduanya mendukung informasi wallet, top-up, transfer, dan riwayat transaksi.

Data disimpan di memory, bukan di database. Perubahan saldo dan riwayat akan hilang saat aplikasi ditutup.

## Fitur

- Memilih akun dan melihat informasi serta saldo wallet.
- Melakukan top-up dan transfer.
- Melihat riwayat transaksi, memfilter Income/Expense, dan melihat totalnya.
- Memvalidasi nominal, saldo, dan wallet tujuan.

## Struktur proyek

```text
java/ewallet/                  # Aplikasi Java (Maven)
└── src/main/java/com/ewallet/
    ├── App.java
    ├── data/
    ├── models/
    ├── services/
    └── Interfaces/

dotnet/
├── EWalletApp.slnx
└── src/EWalletApp/            # Aplikasi .NET
    ├── Program.cs
    ├── Data/
    ├── Interfaces/
    ├── Models/
    └── Services/
```

## Menjalankan aplikasi

### Java

Prasyarat: JDK 25 dan Maven. Versi Java ditentukan oleh `pom.xml`.

```bash
cd java/ewallet
mvn test
mvn compile
java -cp target/classes com.ewallet.App
```

### C# / .NET

Prasyarat: .NET SDK 10.

```bash
dotnet run --project dotnet/src/EWalletApp/EWallet.Console.csproj
```

## Konsep dan teknologi

Kedua program menggunakan penerapan OOP yang sama

- **Inheritance:** Transaction menjadi parent untuk Topup dan Transfer.

- **Abstracton:** Transaction sebagai abstract class untuk childnya dan WalletOperations/IWalletOperations sebagai interface untuk WalletService.

- **Encapsulation:** penggunaan private supaya variabel/objek tidak bisa digunakan oleh class lain.

- **Polymorphism:** Overriding pada method Topup dan Transfer yang ada di dalam interface.

- **Java:** Maven, `BigDecimal`, `List<T>`, dan Stream API.

- **C#/.NET:** .NET SDK, `decimal`, `List<T>`, dan LINQ.
