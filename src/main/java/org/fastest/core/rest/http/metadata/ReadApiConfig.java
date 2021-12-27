package org.fastest.core.rest.http.metadata;

import org.fastest.common.yaml.ListConstructor;
import org.fastest.utils.files.FileUtil;
import org.fastest.utils.files.YamlUtil;
import org.yaml.snakeyaml.TypeDescription;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2020/10/26
 */
public final class ReadApiConfig {
    private static final File root = FileUtil.getResourcesPath();
    private static String folderName = "apiconfig";
    private static final ConcurrentHashMap<String, Map<String, Uri>> serverMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Map<String, Map<String, Uri>>> serverMapByFile = new ConcurrentHashMap<>();
    private static final List<Server> servers = init();

    private ReadApiConfig(){}

    public static String printTemplate(){
        String template = "" +
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
        System.out.println("api配置文件模板：\n"+template);
        return template;
    }

    /**
     * 获取配置文件所有的内容
     * @return 配置文件内容
     */
    public static List<Server> getAllConfig(){
        return servers;
    }

    /**
     * get api config server node
     * @param serverName server name
     * @return server object
     */
    public static Server getServer(String serverName){
        if (Objects.isNull(serverName)) return null;
        for(Server server:servers){
            if(serverName.equals(server.host)){
                return server;
            }
        }
        return null;
    }

    /**
     * get api config api node
     * @param serverName server name
     * @param apiName api name
     * @return api object
     */
    public static Uri getApi(String serverName, String apiName){
        return getApi(serverName, apiName, null);
    }

    /**
     * get api config api node
     * @param serverName server name
     * @param apiName api name
     * @param file apiconfig file path
     * @return api object
     */
    public static Uri getApi(String serverName, String apiName, String file){
        if(Objects.isNull(serverName) || Objects.isNull(apiName)) return null;
        try {
            Map<String, Map<String, Uri>> _serverMap;
            if(Objects.isNull(file)) {
                _serverMap = serverMap;
            }else {
                _serverMap = serverMapByFile.get(new File(root, file).getAbsolutePath());
            }
            return _serverMap.get(serverName).get(serverName + apiName);
        }catch (NullPointerException e){
            return null;
        }
    }

    /**
     * initialization api config, run only one
     * @return api config object
     */
    private static List<Server> init(){
        System.out.println("我是path");
        System.out.println(FileUtil.getResourcesPath());
        String apiConfName = YamlUtil.getString("api.config.folder.path");
        if(Objects.nonNull(apiConfName)){
            folderName = apiConfName;
        }
        List<Server> allFileServers = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        FileUtil.collect(FileUtil.createFolder(root, folderName), fileList, new String[]{".yaml"});
        ListConstructor<Server> constructor = new ListConstructor<>(Server.class);
        TypeDescription customTypeDescription = new TypeDescription(List.class);
        customTypeDescription.addPropertyParameters("uris", Uri.class);
        constructor.addTypeDescription(customTypeDescription);
        fileList.forEach(file -> {
            List<Server> oneFileServers = YamlUtil.toEntity(file.getAbsolutePath(), constructor);
            Map<String, Map<String, Uri>> tmpServerMap = new HashMap<>();
            for(Server server:oneFileServers){
                Map<String, Uri> apiMap = new HashMap<>();
                for(Uri uri :server.uris){
                    uri.setHost(server.host);
                    uri.setUrl(server.host+ uri.uri);
                    apiMap.put(server.serverName+ uri.uriName, uri);
                }
                tmpServerMap.put(server.serverName, apiMap);
                serverMap.put(server.serverName, apiMap);
            }
            serverMapByFile.put(file.getAbsolutePath(), tmpServerMap);
            allFileServers.addAll(oneFileServers);
        });
        return allFileServers;
    }

    public static class Server{
        private String serverName;
        private String desc;
        private String host;
        private List<Uri> uris;

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public List<Uri> getUris() {
            return uris;
        }

        public void setUris(List<Uri> uris) {
            this.uris = uris;
        }

        @Override
        public String toString() {
            return "Server{" +
                    "serverName='" + serverName + '\'' +
                    ", desc='" + desc + '\'' +
                    ", host='" + host + '\'' +
                    ", uris=" + uris +
                    '}';
        }

        public static Server init(){
            Server server = new Server();
            server.setServerName("");
            server.setDesc("");
            server.setHost("");
            server.setUris(Collections.emptyList());
            return server;
        }
    }

    public static class Uri {
        private String uriName;
        private String uri;
        private String host;
        private String method;
        private String desc;
        private String url;

        public String getUriName() {
            return uriName;
        }

        public void setUriName(String uriName) {
            this.uriName = uriName;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Uri{" +
                    "uriName='" + uriName + '\'' +
                    ", uri='" + uri + '\'' +
                    ", host='" + host + '\'' +
                    ", method='" + method + '\'' +
                    ", desc='" + desc + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public static Uri init(){
            Uri uri = new Uri();
            uri.setUrl("");
            uri.setHost("");
            uri.setDesc("");
            uri.setUri("");
            uri.setUriName("");
            uri.setMethod("");
            return uri;
        }
    }
}
