# .NET Core to Java 現代化遷移示範

## 專案概述

這是一個用於 IBM Bob Workshop 的示範專案，展示如何使用 Bob AI 將 .NET Core 應用程式現代化遷移到 Java/Spring Boot，並在軟體開發生命週期（SDLC）中應用 AI 輔助工具。

**學習重點：**
- 使用 Bob AI 進行程式碼遷移
- 理解 .NET Core 到 Java/Spring Boot 的轉換
- 體驗 AI 輔助的 SDLC 流程

## 專案結構

```
modernization-demo/
├── dotnet-api/          # .NET Core 版本（原始系統）
│   ├── Program.cs
│   ├── AccountController.cs
│   ├── AccountService.cs
│   └── KGI.Account.API.csproj
│
├── java-api/            # Java/Spring Boot 版本（現代化目標）
│   └── （將由 Bob AI 協助建立）
│
└── README.md            # 本檔案
```

## API 規格

### 查詢帳戶餘額
**端點：** `GET /api/account/{accountId}/balance`

**回應範例：**
```json
{
  "accountId": "ACC001",
  "balance": 50000.00,
  "currency": "TWD"
}
```

### 查詢交易記錄
**端點：** `GET /api/account/{accountId}/transactions`

**回應範例：**
```json
[
  {
    "date": "2024-03-16T...",
    "amount": -500,
    "description": "ATM 提款",
    "balance": 50000.00
  }
]
```

## 測試資料

| 帳號 | 戶名 | 餘額 (TWD) |
|------|------|-----------|
| ACC001 | 張先生 | 50,000.00 |
| ACC002 | 李小姐 | 120,000.00 |
| ACC003 | 王先生 | 8,500.50 |




## SDLC 重點

### 1. 需求分析
- API 端點定義
- 資料模型設計
- 業務邏輯規格

### 2. 設計階段
- 架構對照（.NET vs Java）
- 框架選擇（ASP.NET Core vs Spring Boot）
- 依賴管理策略

### 3. 開發階段
- 使用 Bob AI 輔助轉換
- 保持業務邏輯一致性
- 確保 API 相容性

### 4. 測試階段
- 功能測試（API 端點）
- 整合測試（業務流程）
- 回歸測試（確保無破壞性變更）

### 5. 部署考量
- 環境配置差異
- 依賴套件管理
- 監控與日誌


## 學習目標

完成此 Workshop 後，您將能夠：

1. ✅ 使用 Bob AI 分析現有程式碼
2. ✅ 理解跨語言遷移的關鍵考量
3. ✅ 在 SDLC 各階段應用 AI 輔助工具
4. ✅ 驗證遷移後的程式碼品質
5. ✅ 確保業務邏輯的一致性

---

**準備好開始 Workshop 了嗎？**

讓我們一起探索 Bob AI 如何加速您的軟體開發生命週期！