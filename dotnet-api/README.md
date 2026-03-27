# 帳戶查詢 API (.NET Core 版本)

## 專案說明

這是原始的 .NET Core 實作，作為現代化遷移的起點。此專案展示了一個簡單但完整的 RESTful API 設計，將使用 Bob AI 遷移到 Java/Spring Boot。

## 專案結構

```
dotnet-api/
├── Program.cs                 # 應用程式進入點、服務配置
├── AccountController.cs       # API 端點定義
├── AccountService.cs          # 業務邏輯實作
└── Account.API.csproj         # 專案配置檔
```

## 業務功能

### 1. 查詢帳戶餘額
**端點：** `GET /api/account/{accountId}/balance`

**業務邏輯：**
- 根據帳號查詢當前餘額
- 回傳帳號、餘額和幣別（TWD）
- 若帳號不存在，回傳 404 錯誤

**回應範例：**
```json
{
  "accountId": "ACC001",
  "balance": 50000.00,
  "currency": "TWD"
}
```

### 2. 查詢交易記錄
**端點：** `GET /api/account/{accountId}/transactions`

**業務邏輯：**
- 查詢指定帳號的近期交易（模擬 3 筆交易）
- 回傳交易日期、金額、描述和交易後餘額
- 若帳號不存在，回傳 404 錯誤

**回應範例：**
```json
[
  {
    "date": "2024-03-23T...",
    "amount": -500,
    "description": "ATM 提款",
    "balance": 50000.00
  },
  {
    "date": "2024-03-22T...",
    "amount": 3000,
    "description": "薪資入帳",
    "balance": 50500.00
  },
  {
    "date": "2024-03-19T...",
    "amount": -1200,
    "description": "信用卡繳費",
    "balance": 47500.00
  }
]
```

## 測試資料

專案使用記憶體內的 Dictionary 模擬帳戶資料：

| 帳號 | 戶名 | 餘額 (TWD) |
|------|------|-----------|
| ACC001 | 張先生 | 50,000.00 |
| ACC002 | 李小姐 | 120,000.00 |
| ACC003 | 王先生 | 8,500.50 |

## 技術架構

### 框架與版本
- **框架：** ASP.NET Core 6.0
- **架構模式：** MVC (Controller + Service)
- **依賴注入：** 內建 DI Container

### 關鍵實作

#### 1. Program.cs - 應用程式進入點
```csharp
var builder = WebApplication.CreateBuilder(args);

// 註冊服務
builder.Services.AddControllers();
builder.Services.AddSingleton<AccountService>();

var app = builder.Build();

// 設定 CORS
app.UseCors(policy => policy
    .AllowAnyOrigin()
    .AllowAnyMethod()
    .AllowAnyHeader());

app.MapControllers();
app.Run();
```

#### 2. AccountController.cs - API 端點
```csharp
[ApiController]
[Route("api/account")]
public class AccountController : ControllerBase
{
    private readonly AccountService _service;
    
    // 建構子注入
    public AccountController(AccountService service)
    {
        _service = service;
    }
    
    // GET /api/account/{accountId}/balance
    [HttpGet("{accountId}/balance")]
    public IActionResult GetBalance(string accountId)
    
    // GET /api/account/{accountId}/transactions
    [HttpGet("{accountId}/transactions")]
    public IActionResult GetTransactions(string accountId)
}
```

#### 3. AccountService.cs - 業務邏輯
```csharp
public class AccountService
{
    // 使用 Dictionary 模擬資料庫
    private static readonly Dictionary<string, decimal> _accounts = new()
    {
        { "ACC001", 50000.00m },
        { "ACC002", 120000.00m },
        { "ACC003", 8500.50m }
    };
    
    public decimal GetBalance(string accountId)
    public bool AccountExists(string accountId)
    public List<Transaction> GetRecentTransactions(string accountId)
}

public class Transaction
{
    public DateTime Date { get; set; }
    public decimal Amount { get; set; }
    public string Description { get; set; }
    public decimal Balance { get; set; }
}
```

## 遷移考量點

### 1. 型別轉換
| .NET Core | Java/Spring Boot | 說明 |
|-----------|------------------|------|
| `decimal` | `BigDecimal` | 金額精度（重要！） |
| `DateTime` | `LocalDateTime` | 日期時間 |
| `Dictionary<K,V>` | `Map<K,V>` | 集合 |
| `List<T>` | `List<T>` | 列表 |

### 2. 註解轉換
| .NET Core | Java/Spring Boot |
|-----------|------------------|
| `[ApiController]` | `@RestController` |
| `[Route("api/account")]` | `@RequestMapping("/api/account")` |
| `[HttpGet("{id}")]` | `@GetMapping("/{id}")` |

### 3. 回應處理
| .NET Core | Java/Spring Boot |
|-----------|------------------|
| `IActionResult` | `ResponseEntity<?>` |
| `Ok(data)` | `ResponseEntity.ok(data)` |
| `NotFound(data)` | `ResponseEntity.notFound().build()` |

### 4. 依賴注入
- .NET: `builder.Services.AddSingleton<AccountService>()`
- Java: `@Service` 註解 + 建構子注入

## 遷移目標

使用 Bob AI 將此專案轉換為 Java/Spring Boot 版本時，需確保：

1. ✅ API 端點路徑完全相同
2. ✅ 回應格式完全相同
3. ✅ 測試資料完全相同
4. ✅ 業務邏輯完全相同
5. ✅ 錯誤處理行為一致

---

**準備好使用 Bob AI 進行遷移了嗎？**

讓我們開始將這個 .NET Core API 轉換為 Java/Spring Boot！