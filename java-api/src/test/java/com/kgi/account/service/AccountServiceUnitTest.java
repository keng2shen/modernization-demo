package com.kgi.account.service;

import com.kgi.account.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AccountService 單元測試
 * 測試帳戶服務的核心業務邏輯
 * 不使用 Spring Boot 容器，純粹的單元測試
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService 單元測試")
class AccountServiceUnitTest {
    
    private AccountService accountService;
    
    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }
    
    /**
     * 測試查詢存在的帳戶餘額
     */
    @Test
    @DisplayName("查詢存在的帳戶餘額應回傳正確金額")
    void testGetBalance_ExistingAccount_ReturnsCorrectBalance() {
        // Act
        BigDecimal balance = accountService.getBalance("ACC001");
        
        // Assert
        assertNotNull(balance);
        assertEquals(new BigDecimal("50000.00"), balance);
    }
    
    /**
     * 測試查詢不存在的帳戶餘額
     */
    @Test
    @DisplayName("查詢不存在的帳戶餘額應回傳零")
    void testGetBalance_NonExistingAccount_ReturnsZero() {
        // Act
        BigDecimal balance = accountService.getBalance("ACC999");
        
        // Assert
        assertNotNull(balance);
        assertEquals(BigDecimal.ZERO, balance);
    }
    
    /**
     * 測試所有測試帳戶的餘額
     */
    @Test
    @DisplayName("所有測試帳戶應回傳正確的餘額")
    void testGetBalance_AllTestAccounts_ReturnCorrectBalances() {
        // Act & Assert
        assertEquals(new BigDecimal("50000.00"), accountService.getBalance("ACC001"));
        assertEquals(new BigDecimal("120000.00"), accountService.getBalance("ACC002"));
        assertEquals(new BigDecimal("8500.50"), accountService.getBalance("ACC003"));
    }
    
    /**
     * 測試查詢 null 帳戶 ID 的餘額
     */
    @Test
    @DisplayName("查詢 null 帳戶 ID 應回傳零")
    void testGetBalance_NullAccountId_ReturnsZero() {
        // Act
        BigDecimal balance = accountService.getBalance(null);
        
        // Assert
        assertEquals(BigDecimal.ZERO, balance);
    }
    
    /**
     * 測試查詢空字串帳戶 ID 的餘額
     */
    @Test
    @DisplayName("查詢空字串帳戶 ID 應回傳零")
    void testGetBalance_EmptyAccountId_ReturnsZero() {
        // Act
        BigDecimal balance = accountService.getBalance("");
        
        // Assert
        assertEquals(BigDecimal.ZERO, balance);
    }
    
    /**
     * 測試檢查存在的帳戶
     */
    @Test
    @DisplayName("檢查存在的帳戶應回傳 true")
    void testAccountExists_ExistingAccounts_ReturnsTrue() {
        // Act & Assert
        assertTrue(accountService.accountExists("ACC001"));
        assertTrue(accountService.accountExists("ACC002"));
        assertTrue(accountService.accountExists("ACC003"));
    }
    
    /**
     * 測試檢查不存在的帳戶
     */
    @Test
    @DisplayName("檢查不存在的帳戶應回傳 false")
    void testAccountExists_NonExistingAccounts_ReturnsFalse() {
        // Act & Assert
        assertFalse(accountService.accountExists("ACC999"));
        assertFalse(accountService.accountExists("INVALID"));
        assertFalse(accountService.accountExists(""));
    }
    
    /**
     * 測試檢查 null 帳戶 ID
     */
    @Test
    @DisplayName("檢查 null 帳戶 ID 應回傳 false")
    void testAccountExists_NullAccountId_ReturnsFalse() {
        // Act & Assert
        assertFalse(accountService.accountExists(null));
    }
    
    /**
     * 測試取得交易記錄應回傳 3 筆資料
     */
    @Test
    @DisplayName("取得交易記錄應回傳 3 筆資料")
    void testGetRecentTransactions_ReturnsThreeTransactions() {
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // Assert
        assertNotNull(transactions);
        assertEquals(3, transactions.size());
    }
    
    /**
     * 測試交易記錄的資料結構完整性
     */
    @Test
    @DisplayName("交易記錄的所有欄位都不應為 null")
    void testGetRecentTransactions_AllFieldsNotNull() {
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // Assert
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
    @DisplayName("交易記錄應包含正數和負數金額")
    void testGetRecentTransactions_ContainsPositiveAndNegativeAmounts() {
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // Assert
        // 第一筆應為負數（ATM 提款）
        assertTrue(transactions.get(0).getAmount().compareTo(BigDecimal.ZERO) < 0,
            "第一筆交易應為負數");
        
        // 第二筆應為正數（薪資入帳）
        assertTrue(transactions.get(1).getAmount().compareTo(BigDecimal.ZERO) > 0,
            "第二筆交易應為正數");
        
        // 第三筆應為負數（信用卡繳費）
        assertTrue(transactions.get(2).getAmount().compareTo(BigDecimal.ZERO) < 0,
            "第三筆交易應為負數");
    }
    
    /**
     * 測試交易記錄的描述內容
     */
    @Test
    @DisplayName("交易記錄應包含正確的描述")
    void testGetRecentTransactions_ContainsCorrectDescriptions() {
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // Assert
        assertEquals("ATM 提款", transactions.get(0).getDescription());
        assertEquals("薪資入帳", transactions.get(1).getDescription());
        assertEquals("信用卡繳費", transactions.get(2).getDescription());
    }
    
    /**
     * 測試交易記錄的餘額一致性
     */
    @Test
    @DisplayName("第一筆交易的餘額應等於當前帳戶餘額")
    void testGetRecentTransactions_FirstTransactionBalanceMatchesCurrentBalance() {
        // Arrange
        String accountId = "ACC001";
        BigDecimal currentBalance = accountService.getBalance(accountId);
        
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        
        // Assert
        assertEquals(currentBalance, transactions.get(0).getBalance(),
            "第一筆交易的餘額應等於當前餘額");
    }
    
    /**
     * 測試交易記錄的日期順序
     */
    @Test
    @DisplayName("交易記錄應按日期從新到舊排序")
    void testGetRecentTransactions_OrderedByDateDescending() {
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // Assert
        LocalDateTime firstDate = transactions.get(0).getDate();
        LocalDateTime secondDate = transactions.get(1).getDate();
        LocalDateTime thirdDate = transactions.get(2).getDate();
        
        assertTrue(firstDate.isAfter(secondDate), "第一筆交易日期應晚於第二筆");
        assertTrue(secondDate.isAfter(thirdDate), "第二筆交易日期應晚於第三筆");
    }
    
    /**
     * 測試交易記錄的金額精確度
     */
    @Test
    @DisplayName("交易金額應保持正確的精確度")
    void testGetRecentTransactions_AmountPrecision() {
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // Assert
        assertEquals(new BigDecimal("-500"), transactions.get(0).getAmount());
        assertEquals(new BigDecimal("3000"), transactions.get(1).getAmount());
        assertEquals(new BigDecimal("-1200"), transactions.get(2).getAmount());
    }
    
    /**
     * 測試不同帳戶的交易記錄
     */
    @Test
    @DisplayName("不同帳戶應回傳不同的交易記錄")
    void testGetRecentTransactions_DifferentAccountsReturnDifferentTransactions() {
        // Act
        List<Transaction> transactions1 = accountService.getRecentTransactions("ACC001");
        List<Transaction> transactions2 = accountService.getRecentTransactions("ACC002");
        
        // Assert
        assertNotNull(transactions1);
        assertNotNull(transactions2);
        assertEquals(3, transactions1.size());
        assertEquals(3, transactions2.size());
        
        // 驗證第一筆交易的餘額不同（因為帳戶餘額不同）
        assertNotEquals(transactions1.get(0).getBalance(), transactions2.get(0).getBalance());
    }
    
    /**
     * 測試交易記錄的餘額計算邏輯
     */
    @Test
    @DisplayName("交易記錄的餘額應符合計算邏輯")
    void testGetRecentTransactions_BalanceCalculationLogic() {
        // Arrange
        String accountId = "ACC001";
        
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        
        // Assert
        BigDecimal balance1 = transactions.get(0).getBalance();
        BigDecimal amount1 = transactions.get(0).getAmount();
        BigDecimal balance2 = transactions.get(1).getBalance();
        
        // 第二筆交易的餘額 + 第一筆交易的金額 = 第一筆交易的餘額
        assertEquals(balance1, balance2.add(amount1),
            "餘額計算邏輯應正確");
    }
    
    /**
     * 測試交易記錄不為空
     */
    @Test
    @DisplayName("交易記錄列表不應為空")
    void testGetRecentTransactions_NotEmpty() {
        // Act
        List<Transaction> transactions = accountService.getRecentTransactions("ACC001");
        
        // Assert
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
    }
    
    /**
     * 測試交易記錄的不可變性
     */
    @Test
    @DisplayName("多次查詢應回傳一致的交易記錄數量")
    void testGetRecentTransactions_ConsistentResults() {
        // Act
        List<Transaction> transactions1 = accountService.getRecentTransactions("ACC001");
        List<Transaction> transactions2 = accountService.getRecentTransactions("ACC001");
        
        // Assert
        assertEquals(transactions1.size(), transactions2.size());
    }
}

// Made with Bob