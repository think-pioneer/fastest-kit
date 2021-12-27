package xyz.thinktest.fastest.http;

import okhttp3.Response;
import okhttp3.ResponseBody;
import xyz.thinktest.fastest.common.exceptions.HttpException;
import xyz.thinktest.fastest.common.json.JSONFactory;
import xyz.thinktest.fastest.http.metadata.Header;
import xyz.thinktest.fastest.http.metadata.Headers;
import xyz.thinktest.fastest.http.metadata.Json;
import xyz.thinktest.fastest.logger.FastLogger;
import xyz.thinktest.fastest.logger.FastLoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @Date: 2020/11/15
 * @Desc: http response object
 */

public class Responder {
    private static final FastLogger logger = FastLoggerFactory.getLogger(Responder.class);
    private final Response response;
    private final int httpCode;
    private final ResponseBody body;
    private final String bodyString;
    private final InputStream bodyInputStream;
    private final byte[] bodyBytes;
    public final Asserts asserts;

    Responder(Response response) {
        this.response = response;
        this.httpCode = this.response.code();
        this.body = this.response.body();
        if(Objects.isNull(this.body)){
            throw new HttpException("response body is null");
        }
        try {
            bodyBytes = this.body.bytes();
        }catch (IOException e){
            throw new HttpException("read body exception", e);
        }
        this.bodyString = new String(bodyBytes, StandardCharsets.UTF_8);
        this.bodyInputStream = new ByteArrayInputStream(bodyBytes);
        this.asserts = new Asserts(this.bodyString);
    }

    public void respInfo(){
        logger.info("**********HTTP RESPONSE**********\n" +
                "Http Status Code:{}\n" +
                "Http Response Header:{}\n" +
                "Http Response body:{}", this.httpCode, this.getHeaders(), this.bodyString);
    }

    /**
     * http status code
     * @return http status code
     */
    public int getStatusCode(){
        return this.httpCode;
    }

    /**
     * response body
     * @return response body
     */
    public ResponseBody getBody(){
        return this.body;
    }

    /**
     * get body byte[]
     * @return body byte
     */
    public byte[] getBodyBytes(){
        return this.bodyBytes;
    }

    /**
     * response body(string)(utf-8)
     * @return response body
     */
    public String getBodyString(){
        return this.bodyString;
    }

    /**
     * custom Charset
     * @param charset charset
     * @return string
     */
    public String getBodyString(Charset charset){
        return new String(this.bodyBytes, charset);
    }

    /**
     * response body(json)
     * @return response body
     */
    public Json getBodyJson(){
        Json json = new Json();
        json.append(JSONFactory.stringToJson(this.bodyString));
        return json;
    }

    /**
     * get origin response
     * @return okhttp response
     */
    public Response originalResponse(){
        return this.response;
    }

    /**
     * get all header
     * @return headers
     */
    public Headers getHeaders(){
        Headers headers = new Headers();
        this.response.headers().forEach((e) -> headers.write(new Header(e.getFirst(), e.getSecond())));
        return headers;
    }

    /**
     * get header by key
     * @param key header key
     * @return header value
     */
    public String getHeader(String key){
        return this.response.header(key);
    }

    /**
     * download
     * @param path save file's path. if folder will use response header Content-Disposition value as name
     */
    public void download(String path){
        File file = new File(path);
        if(!file.exists()){
            throw new HttpException("not found path: " + file.getAbsolutePath());
        }

        if(file.isDirectory()){
            String header = this.getHeader("Content-Disposition");
            Pattern pattern = Pattern.compile(".*filename=\"(.*)\"");
            Matcher matcher = pattern.matcher(header);
            if(!matcher.find()){
                throw new HttpException("not found file name form header");
            }
            file = new File(file, matcher.group(1));
        }
        this.download(file);

    }

    /**
     * download execute
     * @param file save file's path
     */
    private void download(File file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            int len;
            byte[] buf = new byte[2048];
            while ((len = this.bodyInputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();
            this.bodyInputStream.close();
        }catch (IOException e){
            throw new HttpException("download fail", e);
        }
    }
}
