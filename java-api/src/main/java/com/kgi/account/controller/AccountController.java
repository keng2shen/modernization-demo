package com.kgi.account.controller;

import com.kgi.account.model.BalanceResponse;
import com.kgi.account.model.ErrorResponse;
import com.kgi.account.model.Transaction;
import com.kgi.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 帳戶查詢 REST 控制器
 * 提供帳戶餘額與交易記錄查詢的 API 端點
 * 對應 .NET Core 的 AccountController 類別
 */
@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountController {
    
    private final AccountService accountService;
    
    /**
     * 建構子注入 AccountService
     * 
     * @param accountService 帳戶服務實例
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    /**
     * 查詢帳戶餘額
     * 端點：GET /api/account/{accountId}/balance
     * 
     * @param accountId 帳戶編號
     * @return 成功時回傳 200 OK 與餘額資訊，失敗時回傳 404 Not Found
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountId) {
        // 檢查帳戶是否存在
        if (!accountService.accountExists(accountId)) {
            ErrorResponse errorResponse = new ErrorResponse("帳戶不存在", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        
        // 取得帳戶餘額
        BigDecimal balance = accountService.getBalance(accountId);
        BalanceResponse response = new BalanceResponse(accountId, balance, "TWD");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 查詢交易記錄
     * 端點：GET /api/account/{accountId}/transactions
     * 
     * @param accountId 帳戶編號
     * @return 成功時回傳 200 OK 與交易記錄列表，失敗時回傳 404 Not Found
     */
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable String accountId) {
        // 檢查帳戶是否存在
        if (!accountService.accountExists(accountId)) {
            ErrorResponse errorResponse = new ErrorResponse("帳戶不存在", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        
        // 取得交易記錄
        List<Transaction> transactions = accountService.getRecentTransactions(accountId);
        
        return ResponseEntity.ok(transactions);
    }
}

// Made with Bob
