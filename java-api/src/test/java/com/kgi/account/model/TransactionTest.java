package com.kgi.account.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Transaction 模型類別單元測試
 * 測試交易記錄資料模型的建構與存取方法
 */
@DisplayName("Transaction 模型測試")
class TransactionTest {
    
    /**
     * 測試預設建構子
     */
    @Test
    @DisplayName("預設建構子應建立空的 Transaction 物件")
    void testDefaultConstructor() {
        // Act
        Transaction transaction = new Transaction();
        
        // Assert
        assertNotNull(transaction);
        assertNull(transaction.getDate());
        assertNull(transaction.getAmount());
        assertNull(transaction.getDescription());
        assertNull(transaction.getBalance());
    }
    
    /**
     * 測試完整建構子
     */
    @Test
    @DisplayName("完整建構子應正確設定所有欄位")
    void testFullConstructor() {
        // Arrange
        LocalDateTime date = LocalDateTime.now();
        BigDecimal amount = new BigDecimal("1000.00");
        String description = "測試交易";
        BigDecimal balance = new BigDecimal("50000.00");
        
        // Act
        Transaction transaction = new Transaction(date, amount, description, balance);
        
        // Assert
        assertNotNull(transaction);
        assertEquals(date, transaction.getDate());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(balance, transaction.getBalance());
    }
    
    /**
     * 測試 Setter 方法
     */
    @Test
    @DisplayName("Setter 方法應正確設定欄位值")
    void testSetters() {
        // Arrange
        Transaction transaction = new Transaction();
        LocalDateTime date = LocalDateTime.now();
        BigDecimal amount = new BigDecimal("2000.00");
        String description = "薪資入帳";
        BigDecimal balance = new BigDecimal("60000.00");
        
        // Act
        transaction.setDate(date);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setBalance(balance);
        
        // Assert
        assertEquals(date, transaction.getDate());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(balance, transaction.getBalance());
    }
    
    /**
     * 測試正數金額交易（入帳）
     */
    @Test
    @DisplayName("應正確處理正數金額交易")
    void testPositiveAmount() {
        // Arrange
        BigDecimal positiveAmount = new BigDecimal("5000.00");
        
        // Act
        Transaction transaction = new Transaction(
            LocalDateTime.now(),
            positiveAmount,
            "存款",
            new BigDecimal("55000.00")
        );
        
        // Assert
        assertTrue(transaction.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }
    
    /**
     * 測試負數金額交易（支出）
     */
    @Test
    @DisplayName("應正確處理負數金額交易")
    void testNegativeAmount() {
        // Arrange
        BigDecimal negativeAmount = new BigDecimal("-500.00");
        
        // Act
        Transaction transaction = new Transaction(
            LocalDateTime.now(),
            negativeAmount,
            "ATM 提款",
            new BigDecimal("49500.00")
        );
        
        // Assert
        assertTrue(transaction.getAmount().compareTo(BigDecimal.ZERO) < 0);
    }
    
    /**
     * 測試零金額交易
     */
    @Test
    @DisplayName("應正確處理零金額交易")
    void testZeroAmount() {
        // Arrange
        BigDecimal zeroAmount = BigDecimal.ZERO;
        
        // Act
        Transaction transaction = new Transaction(
            LocalDateTime.now(),
            zeroAmount,
            "查詢餘額",
            new BigDecimal("50000.00")
        );
        
        // Assert
        assertEquals(BigDecimal.ZERO, transaction.getAmount());
    }
    
    /**
     * 測試日期時間精確度
     */
    @Test
    @DisplayName("應保持日期時間的精確度")
    void testDateTimePrecision() {
        // Arrange
        LocalDateTime specificDate = LocalDateTime.of(2024, 3, 15, 14, 30, 45);
        
        // Act
        Transaction transaction = new Transaction(
            specificDate,
            new BigDecimal("1000.00"),
            "測試",
            new BigDecimal("50000.00")
        );
        
        // Assert
        assertEquals(2024, transaction.getDate().getYear());
        assertEquals(3, transaction.getDate().getMonthValue());
        assertEquals(15, transaction.getDate().getDayOfMonth());
        assertEquals(14, transaction.getDate().getHour());
        assertEquals(30, transaction.getDate().getMinute());
        assertEquals(45, transaction.getDate().getSecond());
    }
    
    /**
     * 測試描述欄位可包含中文
     */
    @Test
    @DisplayName("描述欄位應支援中文字元")
    void testChineseDescription() {
        // Arrange
        String chineseDescription = "信用卡繳費 - 台北101";
        
        // Act
        Transaction transaction = new Transaction(
            LocalDateTime.now(),
            new BigDecimal("-1200.00"),
            chineseDescription,
            new BigDecimal("48800.00")
        );
        
        // Assert
        assertEquals(chineseDescription, transaction.getDescription());
    }
    
    /**
     * 測試金額精確度
     */
    @Test
    @DisplayName("應保持金額的小數精確度")
    void testAmountPrecision() {
        // Arrange
        BigDecimal preciseAmount = new BigDecimal("1234.56");
        
        // Act
        Transaction transaction = new Transaction(
            LocalDateTime.now(),
            preciseAmount,
            "測試",
            new BigDecimal("51234.56")
        );
        
        // Assert
        assertEquals(0, preciseAmount.compareTo(transaction.getAmount()));
        assertEquals(2, transaction.getAmount().scale());
    }
    
    /**
     * 測試餘額精確度
     */
    @Test
    @DisplayName("應保持餘額的小數精確度")
    void testBalancePrecision() {
        // Arrange
        BigDecimal preciseBalance = new BigDecimal("8500.50");
        
        // Act
        Transaction transaction = new Transaction(
            LocalDateTime.now(),
            new BigDecimal("100.50"),
            "測試",
            preciseBalance
        );
        
        // Assert
        assertEquals(0, preciseBalance.compareTo(transaction.getBalance()));
    }
    
    /**
     * 測試空描述
     */
    @Test
    @DisplayName("應允許空描述")
    void testEmptyDescription() {
        // Act
        Transaction transaction = new Transaction(
            LocalDateTime.now(),
            new BigDecimal("1000.00"),
            "",
            new BigDecimal("51000.00")
        );
        
        // Assert
        assertEquals("", transaction.getDescription());
    }
    
    /**
     * 測試 null 值處理
     */
    @Test
    @DisplayName("應允許設定 null 值")
    void testNullValues() {
        // Act
        Transaction transaction = new Transaction(null, null, null, null);
        
        // Assert
        assertNull(transaction.getDate());
        assertNull(transaction.getAmount());
        assertNull(transaction.getDescription());
        assertNull(transaction.getBalance());
    }
}

// Made with Bob