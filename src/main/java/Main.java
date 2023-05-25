import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        String filePath = "sitemap.txt";
        Path file = Path.of(filePath);

        System.out.println("Начинаю создавать карту сайта");
        try {
            Files.delete(file);
            System.out.println("Файл успешно очищен.");
        } catch (IOException e) {
            System.out.println("Ошибка при очистке файла: " + e.getMessage());
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        WebsiteCrawler crawler = new WebsiteCrawler("https://skillbox.ru/", filePath, 0);
        forkJoinPool.invoke(crawler);
        System.out.println("Карта сайта закончена");
    }
}
