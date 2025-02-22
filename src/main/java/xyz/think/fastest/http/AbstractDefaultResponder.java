package xyz.think.fastest.http;

import okhttp3.Response;
import okhttp3.ResponseBody;
import xyz.think.fastest.common.exceptions.HttpException;
import xyz.think.fastest.common.json.JSONFactory;
import xyz.think.fastest.http.metadata.Header;
import xyz.think.fastest.http.metadata.Headers;
import xyz.think.fastest.http.metadata.Json;
import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.logger.FastestLoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractDefaultResponder implements Responder{
    private static final FastestLogger logger = FastestLoggerFactory.getLogger(AbstractDefaultResponder.class);
    private Response response;
    private int httpCode;
    private ResponseBody body;
    private String bodyString;
    private InputStream bodyInputStream;
    private byte[] bodyBytes;
    public Asserts asserts;

    AbstractDefaultResponder(){}

    AbstractDefaultResponder(Response response){
        this.init(response);
    }

    @Override
    public void init(Response response){
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

    @Override
    public int stateCode() {
        return this.httpCode;
    }

    @Override
    public ResponseBody body() {
        return this.body;
    }

    @Override
    public byte[] bodyToBytes() {
        return this.bodyBytes;
    }

    @Override
    public String bodyToString() {
        return this.bodyString;
    }

    @Override
    public String bodyToString(Charset charset) {
        return new String(this.bodyBytes, charset);
    }

    @Override
    public Json bodyToJson() {
        Json json = Json.newEmpty();
        json.append(JSONFactory.stringToJson(this.bodyString));
        return json;
    }

    @Override
    public Response originalResponse() {
        return this.response;
    }

    @Override
    public Headers headers() {
        Headers headers = Headers.newEmpty();
        this.response.headers().forEach((e) -> headers.write(new Header(e.getFirst(), e.getSecond())));
        return headers;
    }

    @Override
    public String header(String key) {
        return this.response.header(key);
    }

    @Override
    public void download(String path) {
        File file = new File(path);
        if(!file.exists()){
            throw new HttpException("not found path: " + file.getAbsolutePath());
        }

        if(file.isDirectory()){
            String header = this.header("Content-Disposition");
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

    public Asserts asserts(){
        return this.asserts;
    }
}
