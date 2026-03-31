package com.kgi.account.service;

import com.kgi.account.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AccountService 單元測試
 * 測試帳戶服務的核心業務邏輯
 */
@SpringBootTest
class AccountServiceTest {
    
    @Autowired
    private AccountService accountService;
    
    /**
     * 測試查詢存在的帳戶餘額
     */
    @Test
    void testGetBalance_ExistingAccount() {
        BigDecimal balance = accountService.getBalance("ACC001");
        assertEquals(new BigDecimal("50000.00"), balance);
    }
    
    /**
     * 測試查詢不存在的帳戶餘額（應回傳 0）
     */
    @Test
    void testGetBalance_NonExistingAccount() {
        BigDecimal balance = accountService.getBalance("ACC999");
        assertEquals(BigDecimal.ZERO, balance);
    }
    
    /**
     * 測試所有測試帳戶的餘額
     */
    @Test
    void testGetBalance_AllTestAccounts() {
        assertEquals(new BigDecimal("50000.00"), accountService.getBalance("ACC001"));
        assertEquals(new BigDecimal("120000.00"), accountService.getBalance("ACC002"));
        assertEquals(new BigDecimal("8500.50"), accountService.getBalance("ACC003"));
    }
    
    /**
     * 測試檢查存在的帳戶
     */
    @Test
    void testAccountExists_ExistingAccount() {
        assertTrue(accountService.accountExists("ACC001"));
        assertTrue(accountService.accountExists("ACC002"));
        assertTrue(accountService.accountExists("ACC003"));
    }
    
    /**
     * 測試檢查不存在的帳戶
     */
    @Test
    void testAccountExists_NonExistingAccount() {
        assertFalse(accountService.accountExists("ACC999"));
        assertFalse(accountService.accountExists("INVALID"));
    }
    
    /**
     * 測試取得交易記錄應回傳 3 筆資料
     */
    @Test
    void testGetRecentTransactions_ReturnsThreeTransactions() {
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        assertEquals(3, transactions.size());
    }
    
    /**
     * 測試交易記錄的資料結構完整性
     */
    @Test
    void testGetRecentTransactions_DataStructure() {
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        for (Transaction transaction : transactions) {
            assertNotNull(transaction.getDate(), "交易日期不應為 null");
            assertNotNull(transaction.getAmount(), "交易金額不應為 null");
            assertNotNull(transaction.getDescription(), "交易描述不應為 null");
            assertNotNull(transaction.getBalance(), "交易後餘額不應為 null");
        }
    }
    
    /**
     * 測試交易記錄的金額類型
     */
    @Test
    void testGetRecentTransactions_AmountTypes() {
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // 第一筆應為負數（ATM 提款）
        assertTrue(transactions.get(0).getAmount().compareTo(BigDecimal.ZERO) < 0);
        
        // 第二筆應為正數（薪資入帳）
        assertTrue(transactions.get(1).getAmount().compareTo(BigDecimal.ZERO) > 0);
        
        // 第三筆應為負數（信用卡繳費）
        assertTrue(transactions.get(2).getAmount().compareTo(BigDecimal.ZERO) < 0);
    }
    
    /**
     * 測試交易記錄的餘額一致性
     */
    @Test
    void testGetRecentTransactions_BalanceConsistency() {
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        BigDecimal currentBalance = accountService.getBalance("ACC001");
        
        // 第一筆交易的餘額應等於當前餘額
        assertEquals(currentBalance, transactions.get(0).getBalance());
    }
}

// Made with Bob
