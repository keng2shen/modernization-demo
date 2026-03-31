package com.kgi.account.service;

import com.kgi.account.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帳戶服務類別
 * 提供帳戶查詢相關的業務邏輯
 * 對應 .NET Core 的 AccountService 類別
 */
@Service
public class AccountService {
    
    /**
     * 帳戶資料儲存（In-Memory）
     * Key: 帳戶編號, Value: 帳戶餘額
     */
    private static final Map<String, BigDecimal> accounts = new HashMap<>();
    
    // 靜態初始化區塊：載入測試資料
    static {
        accounts.put("ACC001", new BigDecimal("50000.00"));
        accounts.put("ACC002", new BigDecimal("120000.00"));
        accounts.put("ACC003", new BigDecimal("8500.50"));
    }
    
    /**
     * 查詢帳戶餘額
     * 
     * @param accountId 帳戶編號
     * @return 帳戶餘額，若帳戶不存在則回傳 0
     */
    public BigDecimal getBalance(String accountId) {
        return accounts.getOrDefault(accountId, BigDecimal.ZERO);
    }
    
    /**
     * 檢查帳戶是否存在
     * 
     * @param accountId 帳戶編號
     * @return true 表示帳戶存在，false 表示不存在
     */
    public boolean accountExists(String accountId) {
        return accounts.containsKey(accountId);
    }
    
    /**
     * 取得近期交易記錄（模擬資料）
     * 產生 3 筆模擬交易記錄，從最新到最舊排序
     * 
     * @param accountId 帳戶編號
     * @return 交易記錄列表
     */
    public List<Transaction> getRecentTransactions(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        BigDecimal currentBalance = getBalance(accountId);
        
        // 交易 1：最新交易（ATM 提款）
        LocalDateTime date1 = LocalDateTime.now().minusDays(1).withHour(10).withMinute(30).withSecond(0).withNano(0);
        BigDecimal amount1 = new BigDecimal("-500");
        String description1 = "ATM 提款";
        transactions.add(new Transaction(date1, amount1, description1, currentBalance));
        
        // 交易 2：薪資入帳
        BigDecimal balanceBeforeTx1 = currentBalance.subtract(amount1);
        LocalDateTime date2 = LocalDateTime.now().minusDays(2).withHour(14).withMinute(20).withSecond(0).withNano(0);
        BigDecimal amount2 = new BigDecimal("3000");
        String description2 = "薪資入帳";
        transactions.add(new Transaction(date2, amount2, description2, balanceBeforeTx1));
        
        // 交易 3：信用卡繳費
        BigDecimal balanceBeforeTx2 = balanceBeforeTx1.subtract(amount2);
        LocalDateTime date3 = LocalDateTime.now().minusDays(5).withHour(9).withMinute(15).withSecond(0).withNano(0);
        BigDecimal amount3 = new BigDecimal("-1200");
        String description3 = "信用卡繳費";
        transactions.add(new Transaction(date3, amount3, description3, balanceBeforeTx2));
        
        return transactions;
    }
}

// Made with Bob
