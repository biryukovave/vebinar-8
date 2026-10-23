package utils;

public class Config {
    // Все настройки хранятся прямо здесь, без чтения внешних файлов
    public static final String BASE_URL  = "http://localhost:8080/";
    public static final String LOGIN_URL = "http://localhost:8080/login";
    public static final String ADMIN_URL = "http://localhost:8080/admin";

    public static final String USERNAME  = "admin";
    public static final String PASSWORD  = "secret123";

    public static final int TIMEOUT = 10;
}