import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HtmlParser {
    private final String url;

    public HtmlParser(String url) {
        this.url = url;
    }

    public Set<String> getUrls() throws IOException {
        Set<String> urls = new HashSet<>();

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        for (Element link : links) {
            String href = link.attr("href");
            if (containsPdfKeyword(href)) {
                continue;
            }
            urls.add(href);
        }

        return urls;
    }

    private boolean containsPdfKeyword(String url) {
        return url.toLowerCase().contains("pdf");
    }
}