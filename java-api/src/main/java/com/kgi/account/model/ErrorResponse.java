package com.kgi.account.model;

/**
 * 錯誤回應資料模型
 * 用於 API 錯誤情況的回應格式（例：404 Not Found）
 */
public class ErrorResponse {
    
    private String error;
    private String accountId;
    
    /**
     * 預設建構子
     */
    public ErrorResponse() {
    }
    
    /**
     * 完整建構子
     * 
     * @param error 錯誤訊息
     * @param accountId 相關的帳戶編號
     */
    public ErrorResponse(String error, String accountId) {
        this.error = error;
        this.accountId = accountId;
    }
    
    // Getters and Setters
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

// Made with Bob
