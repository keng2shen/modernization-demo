# .NET Core 到 Java/Spring Boot 現代化遷移設計文件

## 文件資訊

| 項目 | 內容 |
|------|------|
| **專案名稱** | modernization-demo |
| **相關 Issue** | [#1 將專案從.netcore 轉換為java 程式碼](https://github.com/keng2shen/modernization-demo/issues/1) |
| **文件版本** | 1.0 |
| **建立日期** | 2026-03-31 |
| **目標** | 將 .NET Core 6.0 帳戶查詢 API 遷移至 Java/Spring Boot |

---

## 1. 專案概述

### 1.1 背景
本專案為 IBM Bob Workshop 示範專案，目的是展示如何使用 AI 輔助工具將傳統 .NET Core 應用程式現代化遷移到 Java/Spring Boot 平台，並在軟體開發生命週期（SDLC）中應用 AI 技術。

### 1.2 遷移範圍
- **原始系統**：.NET Core 6.0 RESTful API
- **目標系統**：Java 17+ with Spring Boot 3.x
- **業務領域**：銀行帳戶查詢服務
- **核心功能**：
  - 帳戶餘額查詢
  - 交易記錄查詢

---

## 2. 現有系統分析

### 2.1 架構概覽

```
dotnet-api/
├── Program.cs              # 應用程式進入點、依賴注入配置
├── AccountController.cs    # RESTful API 端點定義
├── AccountService.cs       # 業務邏輯層
└── Account.API.csproj      # 專案配置與依賴管理
```

### 2.2 技術堆疊

| 層級 | .NET Core 技術 | 說明 |
|------|---------------|------|
| **框架** | ASP.NET Core 6.0 | Web API 框架 |
| **架構模式** | MVC (Controller + Service) | 分層架構 |
| **依賴注入** | 內建 DI Container | `builder.Services.AddSingleton<T>()` |
| **路由** | Attribute Routing | `[Route]`, `[HttpGet]` |
| **資料儲存** | In-Memory Dictionary | 模擬資料庫 |

### 2.3 API 規格

#### 2.3.1 查詢帳戶餘額
```
端點：GET /api/account/{accountId}/balance
參數：accountId (string) - 帳戶編號
回應：
  成功 (200 OK):
    {
      "accountId": "ACC001",
      "balance": 50000.00,
      "currency": "TWD"
    }
  失敗 (404 Not Found):
    {
      "error": "帳戶不存在",
      "accountId": "ACC999"
    }
```

#### 2.3.2 查詢交易記錄
```
端點：GET /api/account/{accountId}/transactions
參數：accountId (string) - 帳戶編號
回應：
  成功 (200 OK):
    [
      {
        "date": "2024-03-23T10:30:00",
        "amount": -500,
        "description": "ATM 提款",
        "balance": 50000.00
      },
      {
        "date": "2024-03-22T14:20:00",
        "amount": 3000,
        "description": "薪資入帳",
        "balance": 50500.00
      },
      {
        "date": "2024-03-19T09:15:00",
        "amount": -1200,
        "description": "信用卡繳費",
        "balance": 47500.00
      }
    ]
  失敗 (404 Not Found):
    {
      "error": "帳戶不存在",
      "accountId": "ACC999"
    }
```

### 2.4 測試資料

| 帳號 | 戶名 | 餘額 (TWD) |
|------|------|-----------|
| ACC001 | 張先生 | 50,000.00 |
| ACC002 | 李小姐 | 120,000.00 |
| ACC003 | 王先生 | 8,500.50 |

### 2.5 業務邏輯分析

#### AccountService 核心方法
1. **GetBalance(string accountId)**
   - 功能：查詢指定帳戶餘額
   - 邏輯：從 Dictionary 中取得餘額，不存在則回傳 0
   - 回傳：decimal 型別

2. **AccountExists(string accountId)**
   - 功能：驗證帳戶是否存在
   - 邏輯：檢查 Dictionary 是否包含該 key
   - 回傳：bool 型別

3. **GetRecentTransactions(string accountId)**
   - 功能：取得近期交易記錄（模擬 3 筆）
   - 邏輯：
     - 取得當前餘額
     - 產生 3 筆模擬交易（從最新到最舊）
     - 計算每筆交易後的餘額
   - 回傳：List<Transaction>

---

## 3. 目標系統設計

### 3.1 技術堆疊對照

| 層級 | .NET Core | Java/Spring Boot | 轉換說明 |
|------|-----------|------------------|---------|
| **框架** | ASP.NET Core 6.0 | Spring Boot 3.x | Web 框架 |
| **Java 版本** | .NET 6 | Java 17+ | 語言版本 |
| **依賴注入** | 內建 DI | Spring IoC Container | `@Service`, `@Autowired` |
| **REST 控制器** | `[ApiController]` | `@RestController` | 控制器註解 |
| **路由** | `[Route]`, `[HttpGet]` | `@RequestMapping`, `@GetMapping` | 路由註解 |
| **回應處理** | `IActionResult` | `ResponseEntity<?>` | HTTP 回應 |
| **建置工具** | .csproj (MSBuild) | pom.xml (Maven) 或 build.gradle (Gradle) | 依賴管理 |

### 3.2 資料型別對照

| .NET Core | Java | 轉換考量 |
|-----------|------|---------|
| `decimal` | `BigDecimal` | **關鍵！** 金額精度必須使用 BigDecimal |
| `DateTime` | `LocalDateTime` | 日期時間處理 |
| `Dictionary<K,V>` | `Map<K,V>` (HashMap) | 集合類型 |
| `List<T>` | `List<T>` (ArrayList) | 列表類型 |
| `string` | `String` | 字串類型 |
| `bool` | `boolean` | 布林類型 |

### 3.3 目標架構設計

```
java-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── kgi/
│       │           └── account/
│       │               ├── AccountApplication.java      # Spring Boot 主程式
│       │               ├── controller/
│       │               │   └── AccountController.java   # REST 控制器
│       │               ├── service/
│       │               │   └── AccountService.java      # 業務邏輯
│       │               └── model/
│       │                   ├── BalanceResponse.java     # 餘額回應 DTO
│       │                   ├── Transaction.java         # 交易記錄 DTO
│       │                   └── ErrorResponse.java       # 錯誤回應 DTO
│       └── resources/
│           └── application.properties                   # 應用程式配置
└── pom.xml                                              # Maven 依賴配置
```

### 3.4 核心類別設計

#### 3.4.1 AccountApplication.java
```java
@SpringBootApplication
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
        System.out.println("凱基銀行帳戶查詢 API (Java/Spring Boot) 已啟動");
        System.out.println("API 端點：http://localhost:8080");
    }
}
```

#### 3.4.2 AccountController.java
```java
@RestController
@RequestMapping("/api/account")
public class AccountController {
    
    private final AccountService accountService;
    
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountId) {
        // 實作邏輯
    }
    
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable String accountId) {
        // 實作邏輯
    }
}
```

#### 3.4.3 AccountService.java
```java
@Service
public class AccountService {
    
    private static final Map<String, BigDecimal> accounts = new HashMap<>();
    
    static {
        accounts.put("ACC001", new BigDecimal("50000.00"));
        accounts.put("ACC002", new BigDecimal("120000.00"));
        accounts.put("ACC003", new BigDecimal("8500.50"));
    }
    
    public BigDecimal getBalance(String accountId) {
        // 實作邏輯
    }
    
    public boolean accountExists(String accountId) {
        // 實作邏輯
    }
    
    public List<Transaction> getRecentTransactions(String accountId) {
        // 實作邏輯
    }
}
```

#### 3.4.4 資料模型 (DTOs)

**BalanceResponse.java**
```java
public class BalanceResponse {
    private String accountId;
    private BigDecimal balance;
    private String currency;
    
    // Constructors, Getters, Setters
}
```

**Transaction.java**
```java
public class Transaction {
    private LocalDateTime date;
    private BigDecimal amount;
    private String description;
    private BigDecimal balance;
    
    // Constructors, Getters, Setters
}
```

**ErrorResponse.java**
```java
public class ErrorResponse {
    private String error;
    private String accountId;
    
    // Constructors, Getters, Setters
}
```

### 3.5 配置檔案

#### application.properties
```properties
# Server Configuration
server.port=8080
spring.application.name=account-api

# CORS Configuration
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*

# Logging
logging.level.root=INFO
logging.level.com.kgi.account=DEBUG
```

#### pom.xml (Maven 依賴)
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 4. 驗收標準 (Acceptance Criteria)

### 4.1 功能性需求

#### AC-1: API 端點相容性
- [ ] **GET /api/account/{accountId}/balance** 端點正常運作
- [ ] **GET /api/account/{accountId}/transactions** 端點正常運作
- [ ] 端點路徑與 .NET 版本完全相同
- [ ] HTTP 方法與 .NET 版本完全相同

#### AC-2: 回應格式一致性
- [ ] 餘額查詢回應包含 `accountId`, `balance`, `currency` 欄位
- [ ] 交易記錄回應包含 `date`, `amount`, `description`, `balance` 欄位
- [ ] JSON 欄位命名採用 camelCase（與 .NET 預設相同）
- [ ] 日期格式為 ISO 8601 格式（例：2024-03-23T10:30:00）

#### AC-3: 資料精度要求
- [ ] 金額欄位使用 `BigDecimal` 型別，確保精度
- [ ] 餘額顯示到小數點後 2 位
- [ ] 交易金額支援正負值（正數為入帳，負數為支出）

#### AC-4: 測試資料一致性
- [ ] ACC001 帳戶餘額為 50,000.00 TWD
- [ ] ACC002 帳戶餘額為 120,000.00 TWD
- [ ] ACC003 帳戶餘額為 8,500.50 TWD
- [ ] 交易記錄模擬邏輯與 .NET 版本相同

#### AC-5: 錯誤處理
- [ ] 查詢不存在的帳戶回傳 404 Not Found
- [ ] 錯誤回應包含 `error` 和 `accountId` 欄位
- [ ] 錯誤訊息為「帳戶不存在」

### 4.2 非功能性需求

#### AC-6: 效能要求
- [ ] API 回應時間 < 100ms（本地測試）
- [ ] 支援並發請求（至少 10 個同時請求）

#### AC-7: 程式碼品質
- [ ] 遵循 Java 命名慣例（類別 PascalCase，方法 camelCase）
- [ ] 使用 Spring Boot 最佳實踐（依賴注入、分層架構）
- [ ] 程式碼包含適當的註解（中文或英文）
- [ ] 無編譯警告或錯誤

#### AC-8: 建置與部署
- [ ] 使用 Maven 或 Gradle 進行依賴管理
- [ ] 可透過 `mvn spring-boot:run` 或 `gradle bootRun` 啟動
- [ ] 包含 README.md 說明如何建置與執行

#### AC-9: CORS 配置
- [ ] 允許所有來源（AllowAnyOrigin）
- [ ] 允許所有 HTTP 方法
- [ ] 允許所有 Headers

### 4.3 測試驗收

#### AC-10: 單元測試
- [ ] AccountService 的所有方法有單元測試
- [ ] 測試覆蓋率 > 80%

#### AC-11: 整合測試
- [ ] 測試 GET /api/account/ACC001/balance 回傳正確資料
- [ ] 測試 GET /api/account/ACC001/transactions 回傳 3 筆交易
- [ ] 測試 GET /api/account/ACC999/balance 回傳 404
- [ ] 測試 GET /api/account/ACC999/transactions 回傳 404

#### AC-12: API 相容性測試
- [ ] 使用相同的 curl 命令測試 .NET 和 Java 版本
- [ ] 回應 JSON 結構完全相同（欄位順序可不同）
- [ ] 回應資料值完全相同

---

## 5. 遷移策略

### 5.1 遷移步驟

#### 階段 1：專案初始化（預估 30 分鐘）
1. 建立 Spring Boot 專案結構
2. 配置 Maven/Gradle 依賴
3. 設定 application.properties
4. 建立基本套件結構

#### 階段 2：資料模型轉換（預估 30 分鐘）
1. 建立 Transaction.java
2. 建立 BalanceResponse.java
3. 建立 ErrorResponse.java
4. 確保資料型別正確（BigDecimal, LocalDateTime）

#### 階段 3：業務邏輯遷移（預估 1 小時）
1. 轉換 AccountService.cs → AccountService.java
2. 實作 getBalance() 方法
3. 實作 accountExists() 方法
4. 實作 getRecentTransactions() 方法
5. 初始化測試資料

#### 階段 4：控制器遷移（預估 1 小時）
1. 轉換 AccountController.cs → AccountController.java
2. 實作 getBalance() 端點
3. 實作 getTransactions() 端點
4. 處理錯誤回應

#### 階段 5：測試與驗證（預估 1.5 小時）
1. 撰寫單元測試
2. 撰寫整合測試
3. 執行 API 相容性測試
4. 修正發現的問題

#### 階段 6：文件與交付（預估 30 分鐘）
1. 更新 README.md
2. 撰寫建置與執行說明
3. 記錄已知問題與限制

**總預估時間：4.5 小時**

### 5.2 風險與緩解措施

| 風險 | 影響 | 機率 | 緩解措施 |
|------|------|------|---------|
| 金額精度問題 | 高 | 中 | 使用 BigDecimal，撰寫精度測試 |
| 日期格式不一致 | 中 | 中 | 使用 ISO 8601 格式，配置 Jackson |
| CORS 配置錯誤 | 中 | 低 | 參考 Spring Boot CORS 文件 |
| 依賴版本衝突 | 低 | 低 | 使用 Spring Boot BOM 管理版本 |

---

## 6. 測試計畫

### 6.1 單元測試

#### AccountServiceTest.java
```java
@SpringBootTest
class AccountServiceTest {
    
    @Autowired
    private AccountService accountService;
    
    @Test
    void testGetBalance_ExistingAccount() {
        BigDecimal balance = accountService.getBalance("ACC001");
        assertEquals(new BigDecimal("50000.00"), balance);
    }
    
    @Test
    void testGetBalance_NonExistingAccount() {
        BigDecimal balance = accountService.getBalance("ACC999");
        assertEquals(BigDecimal.ZERO, balance);
    }
    
    @Test
    void testAccountExists_ExistingAccount() {
        assertTrue(accountService.accountExists("ACC001"));
    }
    
    @Test
    void testAccountExists_NonExistingAccount() {
        assertFalse(accountService.accountExists("ACC999"));
    }
    
    @Test
    void testGetRecentTransactions_ReturnsThreeTransactions() {
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        assertEquals(3, transactions.size());
    }
}
```

### 6.2 整合測試

#### AccountControllerIntegrationTest.java
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetBalance_Success() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/balance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC001"))
            .andExpect(jsonPath("$.balance").value(50000.00))
            .andExpect(jsonPath("$.currency").value("TWD"));
    }
    
    @Test
    void testGetBalance_NotFound() throws Exception {
        mockMvc.perform(get("/api/account/ACC999/balance"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("帳戶不存在"))
            .andExpect(jsonPath("$.accountId").value("ACC999"));
    }
    
    @Test
    void testGetTransactions_Success() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(3));
    }
}
```

### 6.3 API 相容性測試

#### 測試腳本（test-api-compatibility.sh）
```bash
#!/bin/bash

echo "測試 .NET API..."
DOTNET_BALANCE=$(curl -s http://localhost:5000/api/account/ACC001/balance)
DOTNET_TRANSACTIONS=$(curl -s http://localhost:5000/api/account/ACC001/transactions)

echo "測試 Java API..."
JAVA_BALANCE=$(curl -s http://localhost:8080/api/account/ACC001/balance)
JAVA_TRANSACTIONS=$(curl -s http://localhost:8080/api/account/ACC001/transactions)

echo "比較回應結構..."
# 使用 jq 比較 JSON 結構
```

---

## 7. 交付清單

### 7.1 程式碼交付
- [ ] `AccountApplication.java` - Spring Boot 主程式
- [ ] `AccountController.java` - REST 控制器
- [ ] `AccountService.java` - 業務邏輯服務
- [ ] `Transaction.java` - 交易記錄模型
- [ ] `BalanceResponse.java` - 餘額回應模型
- [ ] `ErrorResponse.java` - 錯誤回應模型
- [ ] `application.properties` - 應用程式配置
- [ ] `pom.xml` 或 `build.gradle` - 依賴配置

### 7.2 測試交付
- [ ] `AccountServiceTest.java` - 服務層單元測試
- [ ] `AccountControllerIntegrationTest.java` - 控制器整合測試
- [ ] 測試覆蓋率報告

### 7.3 文件交付
- [ ] `README.md` - 專案說明與執行指南
- [ ] `DESIGN_DOCUMENT.md` - 本設計文件
- [ ] API 測試範例（curl 命令）

---

## 8. 成功標準

專案遷移成功的判定標準：

1. ✅ **功能完整性**：所有 API 端點正常運作
2. ✅ **資料一致性**：回應資料與 .NET 版本完全相同
3. ✅ **測試通過率**：所有單元測試與整合測試通過
4. ✅ **程式碼品質**：符合 Java/Spring Boot 最佳實踐
5. ✅ **文件完整性**：包含完整的建置與執行說明
6. ✅ **效能達標**：API 回應時間符合要求

---

## 9. 附錄

### 9.1 參考資源

- [Spring Boot 官方文件](https://spring.io/projects/spring-boot)
- [Spring Web MVC 文件](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Java BigDecimal 最佳實踐](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html)
- [ASP.NET Core 到 Spring Boot 遷移指南](https://learn.microsoft.com/en-us/dotnet/architecture/modernize-desktop/example-migration)

### 9.2 關鍵決策記錄

| 決策 | 理由 | 日期 |
|------|------|------|
| 使用 Spring Boot 3.x | 最新穩定版本，支援 Java 17+ | 2026-03-31 |
| 使用 BigDecimal 處理金額 | 確保金額精度，避免浮點數誤差 | 2026-03-31 |
| 保持 API 路徑不變 | 確保前端相容性，降低整合成本 | 2026-03-31 |
| 使用 Maven 作為建置工具 | 業界標準，生態系統完整 | 2026-03-31 |

### 9.3 已知限制

1. 資料儲存使用 In-Memory Map，重啟後資料會遺失
2. 交易記錄為模擬資料，非真實資料庫查詢
3. 未實作身份驗證與授權機制
4. 未實作資料驗證（輸入驗證）

### 9.4 未來改進建議

1. 整合真實資料庫（PostgreSQL, MySQL）
2. 實作 Spring Security 進行身份驗證
3. 加入 API 文件（Swagger/OpenAPI）
4. 實作日誌記錄與監控（Actuator）
5. 加入快取機制（Redis）
6. 實作 Docker 容器化部署

---

## 10. 簽核

| 角色 | 姓名 | 簽核日期 | 備註 |
|------|------|---------|------|
| 專案負責人 | | | |
| 技術負責人 | | | |
| 測試負責人 | | | |

---

**文件結束**

*本文件由 Bob AI 輔助產生，基於 modernization-demo 專案的 .NET Core 程式碼分析與 GitHub Issue #1 需求。*