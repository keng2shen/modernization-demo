package com.kgi.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 應用程式主類別
 * 對應 .NET Core 的 Program.cs
 * 
 * @SpringBootApplication 註解包含：
 * - @Configuration: 標記為配置類別
 * - @EnableAutoConfiguration: 啟用自動配置
 * - @ComponentScan: 啟用元件掃描
 */
@SpringBootApplication
public class AccountApplication {
    
    /**
     * 應用程式進入點
     * 
     * @param args 命令列參數
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
        
        System.out.println("========================================");
        System.out.println("凱基銀行帳戶查詢 API (Java/Spring Boot) 已啟動");
        System.out.println("API 端點：http://localhost:8080");
        System.out.println("========================================");
        System.out.println("可用的 API 端點：");
        System.out.println("  GET /api/account/{accountId}/balance");
        System.out.println("  GET /api/account/{accountId}/transactions");
        System.out.println("========================================");
        System.out.println("測試帳號：ACC001, ACC002, ACC003");
        System.out.println("========================================");
    }
}

// Made with Bob
