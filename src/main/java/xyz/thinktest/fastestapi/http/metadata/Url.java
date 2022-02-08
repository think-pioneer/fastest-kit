package xyz.thinktest.fastestapi.http.metadata;

import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Objects;

/**
 * @Date: 2020/10/16
 */
public class Url {
    private final String url;
    private String fullUrl;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public Url(String url){
        this.url = url;
    }

    public Url(URL url){
        this.url = url.toString();
    }

    public Url(URI uri){
        this.url = uri.toString();
    }

    public String getUrl(){
        return this.url;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getFullUrl() {
        if(Objects.isNull(fullUrl)){
            return url;
        }
        return fullUrl;
    }

    @Override
    public String toString(){
        return this.url;
    }

    /**
     * change to ascii string
     * @return ascii string
     */
    public String asciiString() {
        String url = Objects.isNull(fullUrl) ? this.url : this.fullUrl;
        if(Objects.isNull(url)){
            return "";
        }

        int n = url.length();
        if (n == 0)
            return url;

        // First check whether we actually need to encode
        for (int i = 0;;) {
            if (url.charAt(i) >= '\u0080')
                break;
            if (++i >= n)
                return url;
        }

        String ns = Normalizer.normalize(url, Normalizer.Form.NFC);
        ByteBuffer bb;
        bb = ByteBuffer.wrap(ns.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        while (bb.hasRemaining()) {
            int b = bb.get() & 0xff;
            if (b >= 0x80) {
                sb.append("%");
                sb.append(hexDigits[(b >> 4) & 0x0f]);
                sb.append(hexDigits[(b) & 0x0f]);
            } else {
                sb.append((char) b);
            }
        }
        return sb.toString();
    }
}
