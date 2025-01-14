package xyz.think.fastest.utils;

import xyz.think.fastest.http.metadata.Forms;
import xyz.think.fastest.http.metadata.Header;
import xyz.think.fastest.http.metadata.Headers;
import xyz.think.fastest.http.metadata.Parameters;

/**
 * @Date: 2021/12/18
 */
public class BulkEdit {
    /**
     * Convert the header copied from the browser into a header object
     */
    public static Headers stringToHeaders(String content){
        content = content.trim();
        Headers headers = Headers.newEmpty();
        for(String element:content.split("\r\n")){
            String[] kv = element.split(":");
            if(kv.length>2) {
                headers.write(new Header(kv[0], kv[1]));
            }
        }
        return headers;
    }

    /**
     *Convert the query paramters copied from the browser into a header object
     */
    public static Parameters stringToParameters(String content){
        content = content.trim();
        Parameters parameters = Parameters.newEmpty();
        for(String element:content.split("\n")){
            String[] kv = element.split(":");
            if(kv.length > 2){
                parameters.write(kv[0], kv[1]);
            }
        }
        return parameters;
    }

    /**
     *Convert the form copied from the browser to a header object
     */
    public static Forms stringToFroms(String content){
        content = content.trim();
        Forms forms = Forms.newEmpty();
        for(String element:content.split("\n")){
            String[] kv = element.split(":");
            if(kv.length > 2){
                forms.write(kv[0], kv[1]);
            }
        }
        return forms;
    }
}
