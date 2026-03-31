package com.kgi.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易記錄資料模型
 * 對應 .NET Core 的 Transaction 類別
 */
public class Transaction {
    
    private LocalDateTime date;
    private BigDecimal amount;
    private String description;
    private BigDecimal balance;
    
    /**
     * 預設建構子
     */
    public Transaction() {
    }
    
    /**
     * 完整建構子
     * 
     * @param date 交易日期時間
     * @param amount 交易金額（正數為入帳，負數為支出）
     * @param description 交易描述
     * @param balance 交易後餘額
     */
    public Transaction(LocalDateTime date, BigDecimal amount, String description, BigDecimal balance) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.balance = balance;
    }
    
    // Getters and Setters
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

// Made with Bob
