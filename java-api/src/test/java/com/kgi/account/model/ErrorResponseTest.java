package com.kgi.account.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ErrorResponse 模型類別單元測試
 * 測試錯誤回應資料模型的建構與存取方法
 */
@DisplayName("ErrorResponse 模型測試")
class ErrorResponseTest {
    
    /**
     * 測試預設建構子
     */
    @Test
    @DisplayName("預設建構子應建立空的 ErrorResponse 物件")
    void testDefaultConstructor() {
        // Act
        ErrorResponse response = new ErrorResponse();
        
        // Assert
        assertNotNull(response);
        assertNull(response.getError());
        assertNull(response.getAccountId());
    }
    
    /**
     * 測試完整建構子
     */
    @Test
    @DisplayName("完整建構子應正確設定所有欄位")
    void testFullConstructor() {
        // Arrange
        String error = "帳戶不存在";
        String accountId = "ACC999";
        
        // Act
        ErrorResponse response = new ErrorResponse(error, accountId);
        
        // Assert
        assertNotNull(response);
        assertEquals(error, response.getError());
        assertEquals(accountId, response.getAccountId());
    }
    
    /**
     * 測試 Setter 方法
     */
    @Test
    @DisplayName("Setter 方法應正確設定欄位值")
    void testSetters() {
        // Arrange
        ErrorResponse response = new ErrorResponse();
        String error = "權限不足";
        String accountId = "ACC001";
        
        // Act
        response.setError(error);
        response.setAccountId(accountId);
        
        // Assert
        assertEquals(error, response.getError());
        assertEquals(accountId, response.getAccountId());
    }
    
    /**
     * 測試帳戶不存在錯誤
     */
    @Test
    @DisplayName("應正確處理帳戶不存在錯誤")
    void testAccountNotFoundError() {
        // Act
        ErrorResponse response = new ErrorResponse("帳戶不存在", "ACC999");
        
        // Assert
        assertEquals("帳戶不存在", response.getError());
        assertEquals("ACC999", response.getAccountId());
    }
    
    /**
     * 測試中文錯誤訊息
     */
    @Test
    @DisplayName("應支援中文錯誤訊息")
    void testChineseErrorMessage() {
        // Arrange
        String chineseError = "查詢失敗：系統維護中";
        
        // Act
        ErrorResponse response = new ErrorResponse(chineseError, "ACC001");
        
        // Assert
        assertEquals(chineseError, response.getError());
    }
    
    /**
     * 測試英文錯誤訊息
     */
    @Test
    @DisplayName("應支援英文錯誤訊息")
    void testEnglishErrorMessage() {
        // Arrange
        String englishError = "Account not found";
        
        // Act
        ErrorResponse response = new ErrorResponse(englishError, "ACC999");
        
        // Assert
        assertEquals(englishError, response.getError());
    }
    
    /**
     * 測試長錯誤訊息
     */
    @Test
    @DisplayName("應正確處理長錯誤訊息")
    void testLongErrorMessage() {
        // Arrange
        String longError = "查詢失敗：系統目前正在進行維護作業，預計於 2024-03-15 14:00 完成，請稍後再試。如有緊急需求，請聯繫客服專線 0800-123-456。";
        
        // Act
        ErrorResponse response = new ErrorResponse(longError, "ACC001");
        
        // Assert
        assertEquals(longError, response.getError());
    }
    
    /**
     * 測試不同的帳戶 ID 格式
     */
    @Test
    @DisplayName("應支援不同格式的帳戶 ID")
    void testDifferentAccountIdFormats() {
        // Arrange & Act
        ErrorResponse response1 = new ErrorResponse("錯誤", "ACC001");
        ErrorResponse response2 = new ErrorResponse("錯誤", "12345678");
        ErrorResponse response3 = new ErrorResponse("錯誤", "ACC-001-2024");
        
        // Assert
        assertEquals("ACC001", response1.getAccountId());
        assertEquals("12345678", response2.getAccountId());
        assertEquals("ACC-001-2024", response3.getAccountId());
    }
    
    /**
     * 測試空字串錯誤訊息
     */
    @Test
    @DisplayName("應允許空字串錯誤訊息")
    void testEmptyErrorMessage() {
        // Act
        ErrorResponse response = new ErrorResponse("", "ACC001");
        
        // Assert
        assertEquals("", response.getError());
    }
    
    /**
     * 測試空字串帳戶 ID
     */
    @Test
    @DisplayName("應允許空字串帳戶 ID")
    void testEmptyAccountId() {
        // Act
        ErrorResponse response = new ErrorResponse("帳戶不存在", "");
        
        // Assert
        assertEquals("", response.getAccountId());
    }
    
    /**
     * 測試 null 錯誤訊息
     */
    @Test
    @DisplayName("應允許 null 錯誤訊息")
    void testNullErrorMessage() {
        // Act
        ErrorResponse response = new ErrorResponse(null, "ACC001");
        
        // Assert
        assertNull(response.getError());
    }
    
    /**
     * 測試 null 帳戶 ID
     */
    @Test
    @DisplayName("應允許 null 帳戶 ID")
    void testNullAccountId() {
        // Act
        ErrorResponse response = new ErrorResponse("帳戶不存在", null);
        
        // Assert
        assertNull(response.getAccountId());
    }
    
    /**
     * 測試同時為 null 的情況
     */
    @Test
    @DisplayName("應允許錯誤訊息和帳戶 ID 同時為 null")
    void testBothNull() {
        // Act
        ErrorResponse response = new ErrorResponse(null, null);
        
        // Assert
        assertNull(response.getError());
        assertNull(response.getAccountId());
    }
    
    /**
     * 測試特殊字元處理
     */
    @Test
    @DisplayName("應正確處理特殊字元")
    void testSpecialCharacters() {
        // Arrange
        String errorWithSpecialChars = "錯誤：無效的帳戶 ID \"ACC@#$%\"";
        String accountIdWithSpecialChars = "ACC@#$%";
        
        // Act
        ErrorResponse response = new ErrorResponse(errorWithSpecialChars, accountIdWithSpecialChars);
        
        // Assert
        assertEquals(errorWithSpecialChars, response.getError());
        assertEquals(accountIdWithSpecialChars, response.getAccountId());
    }
    
    /**
     * 測試常見錯誤情境
     */
    @Test
    @DisplayName("應正確處理常見錯誤情境")
    void testCommonErrorScenarios() {
        // Arrange & Act
        ErrorResponse notFound = new ErrorResponse("帳戶不存在", "ACC999");
        ErrorResponse unauthorized = new ErrorResponse("權限不足", "ACC001");
        ErrorResponse systemError = new ErrorResponse("系統錯誤", "ACC002");
        
        // Assert
        assertEquals("帳戶不存在", notFound.getError());
        assertEquals("權限不足", unauthorized.getError());
        assertEquals("系統錯誤", systemError.getError());
    }
    
    /**
     * 測試完整的錯誤回應物件
     */
    @Test
    @DisplayName("應建立完整且有效的錯誤回應物件")
    void testCompleteErrorResponse() {
        // Arrange
        String error = "帳戶不存在";
        String accountId = "ACC999";
        
        // Act
        ErrorResponse response = new ErrorResponse(error, accountId);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getError());
        assertNotNull(response.getAccountId());
        assertFalse(response.getError().isEmpty());
        assertFalse(response.getAccountId().isEmpty());
    }
    
    /**
     * 測試錯誤訊息更新
     */
    @Test
    @DisplayName("應允許更新錯誤訊息")
    void testUpdateErrorMessage() {
        // Arrange
        ErrorResponse response = new ErrorResponse("初始錯誤", "ACC001");
        
        // Act
        response.setError("更新後的錯誤");
        
        // Assert
        assertEquals("更新後的錯誤", response.getError());
    }
    
    /**
     * 測試帳戶 ID 更新
     */
    @Test
    @DisplayName("應允許更新帳戶 ID")
    void testUpdateAccountId() {
        // Arrange
        ErrorResponse response = new ErrorResponse("錯誤", "ACC001");
        
        // Act
        response.setAccountId("ACC002");
        
        // Assert
        assertEquals("ACC002", response.getAccountId());
    }
}

// Made with Bob