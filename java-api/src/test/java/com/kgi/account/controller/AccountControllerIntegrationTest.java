package com.kgi.account.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AccountController 整合測試
 * 測試 REST API 端點的完整功能
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    /**
     * 測試查詢存在的帳戶餘額（成功案例）
     */
    @Test
    void testGetBalance_Success() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/balance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value("ACC001"))
            .andExpect(jsonPath("$.balance").value(50000.00))
            .andExpect(jsonPath("$.currency").value("TWD"));
    }
    
    /**
     * 測試查詢所有測試帳戶的餘額
     */
    @Test
    void testGetBalance_AllTestAccounts() throws Exception {
        // ACC001
        mockMvc.perform(get("/api/account/ACC001/balance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(50000.00));
        
        // ACC002
        mockMvc.perform(get("/api/account/ACC002/balance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(120000.00));
        
        // ACC003
        mockMvc.perform(get("/api/account/ACC003/balance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(8500.50));
    }
    
    /**
     * 測試查詢不存在的帳戶餘額（失敗案例）
     */
    @Test
    void testGetBalance_NotFound() throws Exception {
        mockMvc.perform(get("/api/account/ACC999/balance"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("帳戶不存在"))
            .andExpect(jsonPath("$.accountId").value("ACC999"));
    }
    
    /**
     * 測試查詢交易記錄（成功案例）
     */
    @Test
    void testGetTransactions_Success() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(3));
    }
    
    /**
     * 測試交易記錄的資料結構
     */
    @Test
    void testGetTransactions_DataStructure() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].date").exists())
            .andExpect(jsonPath("$[0].amount").exists())
            .andExpect(jsonPath("$[0].description").exists())
            .andExpect(jsonPath("$[0].balance").exists());
    }
    
    /**
     * 測試交易記錄的描述內容
     */
    @Test
    void testGetTransactions_Descriptions() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("ATM 提款"))
            .andExpect(jsonPath("$[1].description").value("薪資入帳"))
            .andExpect(jsonPath("$[2].description").value("信用卡繳費"));
    }
    
    /**
     * 測試查詢不存在帳戶的交易記錄（失敗案例）
     */
    @Test
    void testGetTransactions_NotFound() throws Exception {
        mockMvc.perform(get("/api/account/ACC999/transactions"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("帳戶不存在"))
            .andExpect(jsonPath("$.accountId").value("ACC999"));
    }
    
    /**
     * 測試 CORS 標頭
     */
    @Test
    void testCorsHeaders() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/balance")
                .header("Origin", "http://localhost:3000"))
            .andExpect(status().isOk())
            .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
    
    /**
     * 測試 Content-Type 為 JSON
     */
    @Test
    void testContentType() throws Exception {
        mockMvc.perform(get("/api/account/ACC001/balance"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }
}

// Made with Bob
