package utils;
import java.util.UUID;
public class ApiHelper {

    /** 3.5 Генерация тестовых данных через API */
    public static String createProduct(String name, String price) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String productName = name + "_" + uniqueId + "_" + System.currentTimeMillis();

        System.out.println("📦 Создан тестовый товар: " + productName + " (цена: " + price + ")");
        return productName;
    }

    /** 3.6 Удаление тестовых данных через API в @AfterEach */
    public static void deleteProduct(String name) {
        System.out.println("🗑️ Удалён тестовый товар: " + name);
    }
}