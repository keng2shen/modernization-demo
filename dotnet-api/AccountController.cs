using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/account")]
public class AccountController : ControllerBase
{
    private readonly AccountService _service;
    
    public AccountController(AccountService service)
    {
        _service = service;
    }
    
    [HttpGet("{accountId}/balance")]
    public IActionResult GetBalance(string accountId)
    {
        var balance = _service.GetBalance(accountId);
        if (balance == 0 && !_service.AccountExists(accountId))
        {
            return NotFound(new { error = "帳戶不存在", accountId });
        }
        return Ok(new { accountId, balance, currency = "TWD" });
    }
    
    [HttpGet("{accountId}/transactions")]
    public IActionResult GetTransactions(string accountId)
    {
        if (!_service.AccountExists(accountId))
        {
            return NotFound(new { error = "帳戶不存在", accountId });
        }
        var transactions = _service.GetRecentTransactions(accountId);
        return Ok(transactions);
    }
}

// Made with Bob
