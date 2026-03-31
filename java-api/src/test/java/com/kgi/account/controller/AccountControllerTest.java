package com.kgi.account.controller;

import com.kgi.account.model.BalanceResponse;
import com.kgi.account.model.ErrorResponse;
import com.kgi.account.model.Transaction;
import com.kgi.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AccountController 單元測試
 * 使用 Mockito 模擬 AccountService 依賴
 * 測試控制器層的邏輯與 HTTP 回應
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountController 單元測試")
class AccountControllerTest {
    
    @Mock
    private AccountService accountService;
    
    @InjectMocks
    private AccountController accountController;
    
    private static final String EXISTING_ACCOUNT_ID = "ACC001";
    private static final String NON_EXISTING_ACCOUNT_ID = "ACC999";
    private static final BigDecimal TEST_BALANCE = new BigDecimal("50000.00");
    
    @BeforeEach
    void setUp() {
        // 每個測試前的初始化（如果需要）
    }
    
    /**
     * 測試查詢存在的帳戶餘額 - 成功案例
     */
    @Test
    @DisplayName("查詢存在的帳戶餘額應回傳 200 OK 與餘額資訊")
    void testGetBalance_ExistingAccount_ReturnsOk() {
        // Arrange
        when(accountService.accountExists(EXISTING_ACCOUNT_ID)).thenReturn(true);
        when(accountService.getBalance(EXISTING_ACCOUNT_ID)).thenReturn(TEST_BALANCE);
        
        // Act
        ResponseEntity<?> response = accountController.getBalance(EXISTING_ACCOUNT_ID);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof BalanceResponse);
        
        BalanceResponse balanceResponse = (BalanceResponse) response.getBody();
        assertEquals(EXISTING_ACCOUNT_ID, balanceResponse.getAccountId());
        assertEquals(TEST_BALANCE, balanceResponse.getBalance());
        assertEquals("TWD", balanceResponse.getCurrency());
        
        // Verify interactions
        verify(accountService, times(1)).accountExists(EXISTING_ACCOUNT_ID);
        verify(accountService, times(1)).getBalance(EXISTING_ACCOUNT_ID);
    }
    
    /**
     * 測試查詢不存在的帳戶餘額 - 失敗案例
     */
    @Test
    @DisplayName("查詢不存在的帳戶餘額應回傳 404 Not Found 與錯誤訊息")
    void testGetBalance_NonExistingAccount_ReturnsNotFound() {
        // Arrange
        when(accountService.accountExists(NON_EXISTING_ACCOUNT_ID)).thenReturn(false);
        
        // Act
        ResponseEntity<?> response = accountController.getBalance(NON_EXISTING_ACCOUNT_ID);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("帳戶不存在", errorResponse.getError());
        assertEquals(NON_EXISTING_ACCOUNT_ID, errorResponse.getAccountId());
        
        // Verify interactions
        verify(accountService, times(1)).accountExists(NON_EXISTING_ACCOUNT_ID);
        verify(accountService, never()).getBalance(anyString());
    }
    
    /**
     * 測試查詢餘額時帳戶 ID 為 null
     */
    @Test
    @DisplayName("查詢餘額時帳戶 ID 為 null 應正確處理")
    void testGetBalance_NullAccountId() {
        // Arrange
        when(accountService.accountExists(null)).thenReturn(false);
        
        // Act
        ResponseEntity<?> response = accountController.getBalance(null);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountService, times(1)).accountExists(null);
    }
    
    /**
     * 測試查詢餘額時帳戶 ID 為空字串
     */
    @Test
    @DisplayName("查詢餘額時帳戶 ID 為空字串應正確處理")
    void testGetBalance_EmptyAccountId() {
        // Arrange
        String emptyAccountId = "";
        when(accountService.accountExists(emptyAccountId)).thenReturn(false);
        
        // Act
        ResponseEntity<?> response = accountController.getBalance(emptyAccountId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountService, times(1)).accountExists(emptyAccountId);
    }
    
    /**
     * 測試查詢交易記錄 - 成功案例
     */
    @Test
    @DisplayName("查詢存在的帳戶交易記錄應回傳 200 OK 與交易列表")
    void testGetTransactions_ExistingAccount_ReturnsOk() {
        // Arrange
        List<Transaction> mockTransactions = createMockTransactions();
        when(accountService.accountExists(EXISTING_ACCOUNT_ID)).thenReturn(true);
        when(accountService.getRecentTransactions(EXISTING_ACCOUNT_ID)).thenReturn(mockTransactions);
        
        // Act
        ResponseEntity<?> response = accountController.getTransactions(EXISTING_ACCOUNT_ID);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        
        @SuppressWarnings("unchecked")
        List<Transaction> transactions = (List<Transaction>) response.getBody();
        assertEquals(3, transactions.size());
        assertEquals(mockTransactions, transactions);
        
        // Verify interactions
        verify(accountService, times(1)).accountExists(EXISTING_ACCOUNT_ID);
        verify(accountService, times(1)).getRecentTransactions(EXISTING_ACCOUNT_ID);
    }
    
    /**
     * 測試查詢不存在帳戶的交易記錄 - 失敗案例
     */
    @Test
    @DisplayName("查詢不存在的帳戶交易記錄應回傳 404 Not Found")
    void testGetTransactions_NonExistingAccount_ReturnsNotFound() {
        // Arrange
        when(accountService.accountExists(NON_EXISTING_ACCOUNT_ID)).thenReturn(false);
        
        // Act
        ResponseEntity<?> response = accountController.getTransactions(NON_EXISTING_ACCOUNT_ID);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("帳戶不存在", errorResponse.getError());
        assertEquals(NON_EXISTING_ACCOUNT_ID, errorResponse.getAccountId());
        
        // Verify interactions
        verify(accountService, times(1)).accountExists(NON_EXISTING_ACCOUNT_ID);
        verify(accountService, never()).getRecentTransactions(anyString());
    }
    
    /**
     * 測試查詢交易記錄時帳戶 ID 為 null
     */
    @Test
    @DisplayName("查詢交易記錄時帳戶 ID 為 null 應正確處理")
    void testGetTransactions_NullAccountId() {
        // Arrange
        when(accountService.accountExists(null)).thenReturn(false);
        
        // Act
        ResponseEntity<?> response = accountController.getTransactions(null);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountService, times(1)).accountExists(null);
    }
    
    /**
     * 測試查詢空交易記錄列表
     */
    @Test
    @DisplayName("查詢帳戶交易記錄為空列表時應正確回傳")
    void testGetTransactions_EmptyList() {
        // Arrange
        when(accountService.accountExists(EXISTING_ACCOUNT_ID)).thenReturn(true);
        when(accountService.getRecentTransactions(EXISTING_ACCOUNT_ID)).thenReturn(Arrays.asList());
        
        // Act
        ResponseEntity<?> response = accountController.getTransactions(EXISTING_ACCOUNT_ID);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        List<Transaction> transactions = (List<Transaction>) response.getBody();
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }
    
    /**
     * 測試多個不同帳戶的餘額查詢
     */
    @Test
    @DisplayName("查詢多個不同帳戶的餘額應正確處理")
    void testGetBalance_MultipleAccounts() {
        // Arrange
        String account1 = "ACC001";
        String account2 = "ACC002";
        BigDecimal balance1 = new BigDecimal("50000.00");
        BigDecimal balance2 = new BigDecimal("120000.00");
        
        when(accountService.accountExists(account1)).thenReturn(true);
        when(accountService.accountExists(account2)).thenReturn(true);
        when(accountService.getBalance(account1)).thenReturn(balance1);
        when(accountService.getBalance(account2)).thenReturn(balance2);
        
        // Act
        ResponseEntity<?> response1 = accountController.getBalance(account1);
        ResponseEntity<?> response2 = accountController.getBalance(account2);
        
        // Assert
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        
        BalanceResponse balanceResponse1 = (BalanceResponse) response1.getBody();
        BalanceResponse balanceResponse2 = (BalanceResponse) response2.getBody();
        
        assertEquals(balance1, balanceResponse1.getBalance());
        assertEquals(balance2, balanceResponse2.getBalance());
    }
    
    /**
     * 輔助方法：建立模擬交易記錄
     */
    private List<Transaction> createMockTransactions() {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(
            new Transaction(now.minusDays(1), new BigDecimal("-500"), "ATM 提款", TEST_BALANCE),
            new Transaction(now.minusDays(2), new BigDecimal("3000"), "薪資入帳", TEST_BALANCE.add(new BigDecimal("500"))),
            new Transaction(now.minusDays(5), new BigDecimal("-1200"), "信用卡繳費", TEST_BALANCE.add(new BigDecimal("500")).subtract(new BigDecimal("3000")))
        );
    }
}

// Made with Bob