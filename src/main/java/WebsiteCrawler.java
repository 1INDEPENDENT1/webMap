import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

public class WebsiteCrawler extends RecursiveAction {
    private static final int MAX_DEPTH = 3; // Максимальная глубина обхода
    private final String url;
    private final int depth;
    private final Set<String> visitedUrls;
    private final String path;

    public WebsiteCrawler(String url, String path, int depth) {
        this.depth = depth;
        this.url = url;
        visitedUrls = new HashSet<>();
        this.path = path;
    }

    @Override
    protected void compute() {
        if (depth > MAX_DEPTH || visitedUrls.contains(url)) {
            return;
        }

        visitedUrls.add(url);

        String indentation = getIndentation(depth);
        writeLine(indentation + url);

        Set<String> childUrls = getChildUrls(url);
        int newDepth = depth + 1;

        List<WebsiteCrawler> websiteCrawlers = new ArrayList<>();
        for (String childUrl : childUrls) {
            try {
                Thread.sleep((long) (Math.random() * ((185-100) + 1) + 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WebsiteCrawler websiteCrawler = new WebsiteCrawler(childUrl, path, newDepth);
            websiteCrawler.fork();
            websiteCrawlers.add(websiteCrawler);
        }

        for (WebsiteCrawler wc : websiteCrawlers) {
            try {
                wc.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Set<String> getChildUrls(String url) {
        Set<String> childUrls = new HashSet<>();

        try {
            HtmlParser htmlParser = new HtmlParser(url);
            Set<String> urls = htmlParser.getUrls();

            for (String childUrl : urls) {
                if (isValidUrl(childUrl) && isSameDomain(childUrl)) {
                    childUrls.add(childUrl);
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return childUrls;
    }

    private boolean isValidUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private boolean isSameDomain(String childUrl) throws URISyntaxException {
        URI baseURI = new URI(url);
        URI childURI = new URI(childUrl);

        return baseURI.getHost().equals(childURI.getHost());
    }

    private String getIndentation(int depth) {
        return "\t".repeat(Math.max(0, depth));
    }

    private void writeLine(String line) {
        try {
            Path filePath = Path.of(path);
            Files.writeString(filePath, line + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}