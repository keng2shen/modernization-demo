package com.kgi.account.model;

import java.math.BigDecimal;

/**
 * 帳戶餘額查詢回應資料模型
 * 對應 API 端點 GET /api/account/{accountId}/balance 的回應格式
 */
public class BalanceResponse {
    
    private String accountId;
    private BigDecimal balance;
    private String currency;
    
    /**
     * 預設建構子
     */
    public BalanceResponse() {
    }
    
    /**
     * 完整建構子
     * 
     * @param accountId 帳戶編號
     * @param balance 帳戶餘額
     * @param currency 幣別（例：TWD）
     */
    public BalanceResponse(String accountId, BigDecimal balance, String currency) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
    }
    
    // Getters and Setters
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

// Made with Bob
