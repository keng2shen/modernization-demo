var builder = WebApplication.CreateBuilder(args);

// 加入服務
builder.Services.AddControllers();
builder.Services.AddSingleton<AccountService>();

var app = builder.Build();

// 設定 CORS（允許前端測試）
app.UseCors(policy => policy
    .AllowAnyOrigin()
    .AllowAnyMethod()
    .AllowAnyHeader());

app.MapControllers();

Console.WriteLine("凱基銀行帳戶查詢 API (.NET Core) 已啟動");
Console.WriteLine("API 端點：http://localhost:5000");
Console.WriteLine("測試：curl http://localhost:5000/api/account/ACC001/balance");

app.Run();

// Made with Bob
