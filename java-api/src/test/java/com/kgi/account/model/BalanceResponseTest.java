package com.kgi.account.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BalanceResponse 模型類別單元測試
 * 測試帳戶餘額回應資料模型的建構與存取方法
 */
@DisplayName("BalanceResponse 模型測試")
class BalanceResponseTest {
    
    /**
     * 測試預設建構子
     */
    @Test
    @DisplayName("預設建構子應建立空的 BalanceResponse 物件")
    void testDefaultConstructor() {
        // Act
        BalanceResponse response = new BalanceResponse();
        
        // Assert
        assertNotNull(response);
        assertNull(response.getAccountId());
        assertNull(response.getBalance());
        assertNull(response.getCurrency());
    }
    
    /**
     * 測試完整建構子
     */
    @Test
    @DisplayName("完整建構子應正確設定所有欄位")
    void testFullConstructor() {
        // Arrange
        String accountId = "ACC001";
        BigDecimal balance = new BigDecimal("50000.00");
        String currency = "TWD";
        
        // Act
        BalanceResponse response = new BalanceResponse(accountId, balance, currency);
        
        // Assert
        assertNotNull(response);
        assertEquals(accountId, response.getAccountId());
        assertEquals(balance, response.getBalance());
        assertEquals(currency, response.getCurrency());
    }
    
    /**
     * 測試 Setter 方法
     */
    @Test
    @DisplayName("Setter 方法應正確設定欄位值")
    void testSetters() {
        // Arrange
        BalanceResponse response = new BalanceResponse();
        String accountId = "ACC002";
        BigDecimal balance = new BigDecimal("120000.00");
        String currency = "USD";
        
        // Act
        response.setAccountId(accountId);
        response.setBalance(balance);
        response.setCurrency(currency);
        
        // Assert
        assertEquals(accountId, response.getAccountId());
        assertEquals(balance, response.getBalance());
        assertEquals(currency, response.getCurrency());
    }
    
    /**
     * 測試台幣幣別
     */
    @Test
    @DisplayName("應正確處理台幣幣別")
    void testTWDCurrency() {
        // Act
        BalanceResponse response = new BalanceResponse(
            "ACC001",
            new BigDecimal("50000.00"),
            "TWD"
        );
        
        // Assert
        assertEquals("TWD", response.getCurrency());
    }
    
    /**
     * 測試其他幣別
     */
    @Test
    @DisplayName("應支援多種幣別")
    void testMultipleCurrencies() {
        // Arrange & Act
        BalanceResponse usdResponse = new BalanceResponse("ACC001", new BigDecimal("1000.00"), "USD");
        BalanceResponse eurResponse = new BalanceResponse("ACC002", new BigDecimal("2000.00"), "EUR");
        BalanceResponse jpyResponse = new BalanceResponse("ACC003", new BigDecimal("100000"), "JPY");
        
        // Assert
        assertEquals("USD", usdResponse.getCurrency());
        assertEquals("EUR", eurResponse.getCurrency());
        assertEquals("JPY", jpyResponse.getCurrency());
    }
    
    /**
     * 測試零餘額
     */
    @Test
    @DisplayName("應正確處理零餘額")
    void testZeroBalance() {
        // Act
        BalanceResponse response = new BalanceResponse(
            "ACC001",
            BigDecimal.ZERO,
            "TWD"
        );
        
        // Assert
        assertEquals(BigDecimal.ZERO, response.getBalance());
    }
    
    /**
     * 測試大額餘額
     */
    @Test
    @DisplayName("應正確處理大額餘額")
    void testLargeBalance() {
        // Arrange
        BigDecimal largeBalance = new BigDecimal("9999999999.99");
        
        // Act
        BalanceResponse response = new BalanceResponse(
            "ACC001",
            largeBalance,
            "TWD"
        );
        
        // Assert
        assertEquals(0, largeBalance.compareTo(response.getBalance()));
    }
    
    /**
     * 測試小數精確度
     */
    @Test
    @DisplayName("應保持餘額的小數精確度")
    void testBalancePrecision() {
        // Arrange
        BigDecimal preciseBalance = new BigDecimal("8500.50");
        
        // Act
        BalanceResponse response = new BalanceResponse(
            "ACC003",
            preciseBalance,
            "TWD"
        );
        
        // Assert
        assertEquals(0, preciseBalance.compareTo(response.getBalance()));
        assertEquals(2, response.getBalance().scale());
    }
    
    /**
     * 測試帳戶 ID 格式
     */
    @Test
    @DisplayName("應支援不同格式的帳戶 ID")
    void testAccountIdFormats() {
        // Arrange & Act
        BalanceResponse response1 = new BalanceResponse("ACC001", new BigDecimal("1000"), "TWD");
        BalanceResponse response2 = new BalanceResponse("12345678", new BigDecimal("2000"), "TWD");
        BalanceResponse response3 = new BalanceResponse("ACC-001-2024", new BigDecimal("3000"), "TWD");
        
        // Assert
        assertEquals("ACC001", response1.getAccountId());
        assertEquals("12345678", response2.getAccountId());
        assertEquals("ACC-001-2024", response3.getAccountId());
    }
    
    /**
     * 測試空字串處理
     */
    @Test
    @DisplayName("應允許空字串值")
    void testEmptyStrings() {
        // Act
        BalanceResponse response = new BalanceResponse("", new BigDecimal("1000"), "");
        
        // Assert
        assertEquals("", response.getAccountId());
        assertEquals("", response.getCurrency());
    }
    
    /**
     * 測試 null 值處理
     */
    @Test
    @DisplayName("應允許設定 null 值")
    void testNullValues() {
        // Act
        BalanceResponse response = new BalanceResponse(null, null, null);
        
        // Assert
        assertNull(response.getAccountId());
        assertNull(response.getBalance());
        assertNull(response.getCurrency());
    }
    
    /**
     * 測試負數餘額（透支情況）
     */
    @Test
    @DisplayName("應正確處理負數餘額")
    void testNegativeBalance() {
        // Arrange
        BigDecimal negativeBalance = new BigDecimal("-1000.00");
        
        // Act
        BalanceResponse response = new BalanceResponse(
            "ACC001",
            negativeBalance,
            "TWD"
        );
        
        // Assert
        assertTrue(response.getBalance().compareTo(BigDecimal.ZERO) < 0);
    }
    
    /**
     * 測試完整的回應物件
     */
    @Test
    @DisplayName("應建立完整且有效的回應物件")
    void testCompleteResponse() {
        // Arrange
        String accountId = "ACC001";
        BigDecimal balance = new BigDecimal("50000.00");
        String currency = "TWD";
        
        // Act
        BalanceResponse response = new BalanceResponse(accountId, balance, currency);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertNotNull(response.getBalance());
        assertNotNull(response.getCurrency());
        assertFalse(response.getAccountId().isEmpty());
        assertFalse(response.getCurrency().isEmpty());
        assertTrue(response.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }
}

// Made with Bob