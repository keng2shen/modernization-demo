public class AccountService
{
    // 模擬銀行的帳戶資料
    private static readonly Dictionary<string, decimal> _accounts = new()
    {
        { "ACC001", 50000.00m },   // 張先生的帳戶
        { "ACC002", 120000.00m },  // 李小姐的帳戶
        { "ACC003", 8500.50m }     // 王先生的帳戶
    };
    
    public decimal GetBalance(string accountId)
    {
        return _accounts.GetValueOrDefault(accountId, 0);
    }
    
    public bool AccountExists(string accountId)
    {
        return _accounts.ContainsKey(accountId);
    }
    
    public List<Transaction> GetRecentTransactions(string accountId)
    {
        // 回傳模擬的交易記錄（從最新到最舊）
        var currentBalance = GetBalance(accountId);
        
        return new List<Transaction>
        {
            new Transaction
            {
                Date = DateTime.Now.AddDays(-1),
                Amount = -500,
                Description = "ATM 提款",
                Balance = currentBalance  // 交易後餘額：50000
            },
            new Transaction
            {
                Date = DateTime.Now.AddDays(-2),
                Amount = 3000,
                Description = "薪資入帳",
                Balance = currentBalance + 500  // 交易後餘額：50500
            },
            new Transaction
            {
                Date = DateTime.Now.AddDays(-5),
                Amount = -1200,
                Description = "信用卡繳費",
                Balance = currentBalance + 500 - 3000  // 交易後餘額：47500
            }
        };
    }
}

public class Transaction
{
    public DateTime Date { get; set; }
    public decimal Amount { get; set; }
    public string Description { get; set; } = string.Empty;
    public decimal Balance { get; set; }
}

// Made with Bob
