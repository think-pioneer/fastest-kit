package xyz.think.fastest.http;

import xyz.think.fastest.common.exceptions.FastestBasicException;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * http api config 模板
 */
public final class ApiConfigTemplate {
    private static final String TEMPLATE = "" +
            "- serverName: user_system\n" +
            "  desc: 用户系统服务\n" +
            "  host: 'http://user.testdemo.com'\n" +
            "  uris:\n" +
            "    - uriName: get_userinfo_by_id\n" +
            "      uri: /api/userinfo\n" +
            "      method: get\n" +
            "      desc: 通过id获取用户信息\n" +
            "    - uriName: get_userinfo_by_id_restful\n" +
            "      uri: '/api/{id}/userinfo'\n" +
            "      method: get\n" +
            "      desc: 通过id获取用户信息(restful)";

    private ApiConfigTemplate(){}

    /**
     * 打印api config 模板信息。并将模板信息写入OutputStream中
     * @param out OutputStream
     */
    public static void printTemplate(OutputStream out){
        System.out.println("api config template：\n"+ApiConfigTemplate.TEMPLATE);
        try {
            if (out != null) {
                out.write(ApiConfigTemplate.TEMPLATE.getBytes(StandardCharsets.UTF_8));
            }
        }catch (Throwable cause) {
            throw new FastestBasicException(cause);
        }
    }

    /**
     * @see #printTemplate(OutputStream)
     */
    public static void printTemplate(){
        printTemplate(null);
    }
}
