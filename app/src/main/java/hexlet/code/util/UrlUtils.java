package hexlet.code.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {
    public static String normalizeUrl(String str) throws URISyntaxException, MalformedURLException {
        var uri = new URI(str);
        var url = uri.toURL();
        return url.getProtocol() + "://" + url.getHost() + (url.getPort() == -1 ? "" : url.getPort());
    }
}
