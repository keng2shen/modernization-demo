# 凱基銀行帳戶查詢 API - Java/Spring Boot 版本

這是從 .NET Core 遷移到 Java/Spring Boot 的帳戶查詢 API 專案。

## 專案資訊

- **框架**: Spring Boot 3.2.3
- **Java 版本**: 17+
- **建置工具**: Maven
- **相關 Issue**: [#1 將專案從.netcore 轉換為java 程式碼](https://github.com/keng2shen/modernization-demo/issues/1)

## 專案結構

```
java-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── kgi/
│   │   │           └── account/
│   │   │               ├── AccountApplication.java      # Spring Boot 主程式
│   │   │               ├── controller/
│   │   │               │   └── AccountController.java   # REST 控制器
│   │   │               ├── service/
│   │   │               │   └── AccountService.java      # 業務邏輯
│   │   │               └── model/
│   │   │                   ├── BalanceResponse.java     # 餘額回應 DTO
│   │   │                   ├── Transaction.java         # 交易記錄 DTO
│   │   │                   └── ErrorResponse.java       # 錯誤回應 DTO
│   │   └── resources/
│   │       └── application.properties                   # 應用程式配置
│   └── test/
│       └── java/
│           └── com/
│               └── kgi/
│                   └── account/
│                       ├── service/
│                       │   └── AccountServiceTest.java              # 服務層單元測試
│                       └── controller/
│                           └── AccountControllerIntegrationTest.java # 控制器整合測試
└── pom.xml                                              # Maven 依賴配置
```

## 系統需求

- **Java**: JDK 17 或更高版本
- **Maven**: 3.6+ 或更高版本

## 安裝與執行

### 1. 確認 Java 版本

```bash
java -version
```

應顯示 Java 17 或更高版本。

### 2. 建置專案

```bash
cd java-api
mvn clean install
```

### 3. 執行應用程式

```bash
mvn spring-boot:run
```

或者，執行打包後的 JAR 檔案：

```bash
java -jar target/account-api-1.0.0.jar
```

### 4. 驗證服務啟動

應用程式啟動後，會在 `http://localhost:8080` 上運行，並顯示以下訊息：

```
========================================
凱基銀行帳戶查詢 API (Java/Spring Boot) 已啟動
API 端點：http://localhost:8080
========================================
可用的 API 端點：
  GET /api/account/{accountId}/balance
  GET /api/account/{accountId}/transactions
========================================
測試帳號：ACC001, ACC002, ACC003
========================================
```

## API 端點

### 1. 查詢帳戶餘額

**端點**: `GET /api/account/{accountId}/balance`

**範例請求**:
```bash
curl http://localhost:8080/api/account/ACC001/balance
```

**成功回應** (200 OK):
```json
{
  "accountId": "ACC001",
  "balance": 50000.00,
  "currency": "TWD"
}
```

**失敗回應** (404 Not Found):
```json
{
  "error": "帳戶不存在",
  "accountId": "ACC999"
}
```

### 2. 查詢交易記錄

**端點**: `GET /api/account/{accountId}/transactions`

**範例請求**:
```bash
curl http://localhost:8080/api/account/ACC001/transactions
```

**成功回應** (200 OK):
```json
[
  {
    "date": "2026-03-30T10:30:00",
    "amount": -500,
    "description": "ATM 提款",
    "balance": 50000.00
  },
  {
    "date": "2026-03-29T14:20:00",
    "amount": 3000,
    "description": "薪資入帳",
    "balance": 50500.00
  },
  {
    "date": "2026-03-26T09:15:00",
    "amount": -1200,
    "description": "信用卡繳費",
    "balance": 47500.00
  }
]
```

**失敗回應** (404 Not Found):
```json
{
  "error": "帳戶不存在",
  "accountId": "ACC999"
}
```

## 測試資料

| 帳號 | 戶名 | 餘額 (TWD) |
|------|------|-----------|
| ACC001 | 張先生 | 50,000.00 |
| ACC002 | 李小姐 | 120,000.00 |
| ACC003 | 王先生 | 8,500.50 |

## 執行測試

### 執行所有測試

```bash
mvn test
```

### 執行特定測試類別

```bash
# 執行服務層單元測試
mvn test -Dtest=AccountServiceTest

# 執行控制器整合測試
mvn test -Dtest=AccountControllerIntegrationTest
```

### 查看測試覆蓋率報告

```bash
mvn clean test jacoco:report
```

報告會產生在 `target/site/jacoco/index.html`

## 技術堆疊對照

| 層級 | .NET Core | Java/Spring Boot |
|------|-----------|------------------|
| **框架** | ASP.NET Core 6.0 | Spring Boot 3.2.3 |
| **Java 版本** | .NET 6 | Java 17+ |
| **依賴注入** | 內建 DI | Spring IoC Container |
| **REST 控制器** | `[ApiController]` | `@RestController` |
| **路由** | `[Route]`, `[HttpGet]` | `@RequestMapping`, `@GetMapping` |
| **回應處理** | `IActionResult` | `ResponseEntity<?>` |
| **建置工具** | .csproj (MSBuild) | pom.xml (Maven) |

## 資料型別對照

| .NET Core | Java | 說明 |
|-----------|------|------|
| `decimal` | `BigDecimal` | 金額精度處理 |
| `DateTime` | `LocalDateTime` | 日期時間 |
| `Dictionary<K,V>` | `Map<K,V>` (HashMap) | 集合類型 |
| `List<T>` | `List<T>` (ArrayList) | 列表類型 |
| `string` | `String` | 字串類型 |
| `bool` | `boolean` | 布林類型 |

## 配置說明

### application.properties

```properties
# 伺服器配置
server.port=8080
spring.application.name=account-api

# CORS 配置
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*

# 日誌配置
logging.level.root=INFO
logging.level.com.kgi.account=DEBUG

# JSON 配置
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=Asia/Taipei
```

## 常見問題

### Q1: 如何更改伺服器埠號？

修改 `src/main/resources/application.properties` 中的 `server.port` 設定：

```properties
server.port=9090
```

### Q2: 如何新增測試帳戶？

修改 `AccountService.java` 中的靜態初始化區塊：

```java
static {
    accounts.put("ACC001", new BigDecimal("50000.00"));
    accounts.put("ACC002", new BigDecimal("120000.00"));
    accounts.put("ACC003", new BigDecimal("8500.50"));
    accounts.put("ACC004", new BigDecimal("75000.00")); // 新增帳戶
}
```

### Q3: 如何啟用 HTTPS？

在 `application.properties` 中新增以下配置：

```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-password
server.ssl.key-store-type=PKCS12
```

## 與 .NET Core 版本的差異

1. **金額處理**: 使用 `BigDecimal` 取代 `decimal`，確保金額精度
2. **日期時間**: 使用 `LocalDateTime` 取代 `DateTime`
3. **依賴注入**: 使用 Spring 的 `@Autowired` 取代 .NET 的建構子注入
4. **路由註解**: 使用 `@GetMapping` 取代 `[HttpGet]`
5. **回應處理**: 使用 `ResponseEntity<?>` 取代 `IActionResult`

## 開發指南

### 新增 API 端點

1. 在 `AccountController.java` 中新增方法
2. 使用 `@GetMapping` 或 `@PostMapping` 註解
3. 在 `AccountService.java` 中實作業務邏輯
4. 撰寫對應的單元測試和整合測試

### 程式碼風格

- 類別名稱使用 PascalCase
- 方法名稱使用 camelCase
- 常數使用 UPPER_SNAKE_CASE
- 遵循 Java 命名慣例

## 參考資源

- [Spring Boot 官方文件](https://spring.io/projects/spring-boot)
- [Spring Web MVC 文件](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Java BigDecimal 最佳實踐](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html)
- [設計文件](../DESIGN_DOCUMENT.md)

## 授權

本專案為 IBM Bob Workshop 示範專案。

## 聯絡資訊

如有問題或建議，請在 GitHub Issue 中提出。
