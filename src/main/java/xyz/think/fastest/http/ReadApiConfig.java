package xyz.think.fastest.http;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.yaml.snakeyaml.TypeDescription;
import xyz.think.fastest.common.json.JSONFactory;
import xyz.think.fastest.common.yaml.ListConstructor;
import xyz.think.fastest.utils.files.FileUtil;
import xyz.think.fastest.utils.files.PropertyUtil;
import xyz.think.fastest.utils.files.YamlUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2020/10/26
 */
public final class ReadApiConfig {
    private static final File root = FileUtil.getClassPath();
    private static String folderName = "apiconfig";
    private static final ConcurrentHashMap<String, Map<String, Uri>> serverMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Map<String, Map<String, Uri>>> serverMapByFile = new ConcurrentHashMap<>();
    private static final List<Server> servers = init();

    private ReadApiConfig(){}

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
     * initialization api config, run only once
     * @return api config object
     */
    private static List<Server> init(){
        String apiConfName = PropertyUtil.getProperty("fastest.api.config.folder.path");
        if(Objects.nonNull(apiConfName)){
            folderName = apiConfName;
        }
        List<Server> allFileServers = swagger(folderName);
        List<File> fileList = new ArrayList<>();
        FileUtil.collect(FileUtil.createFolder(root, folderName), fileList, new String[]{".yaml", ".yml", ".json"});
        ListConstructor<Server> constructor = new ListConstructor<>(Server.class);
        TypeDescription customTypeDescription = new TypeDescription(List.class);
        customTypeDescription.addPropertyParameters("uris", Uri.class);
        constructor.addTypeDescription(customTypeDescription);
        JavaType javaType = TypeFactory.defaultInstance().constructParametricType(List.class, Server.class);
        fileList.forEach(file -> {
            List<Server> oneFileServers;
            if(file.getAbsolutePath().endsWith(".json")){
                oneFileServers = JSONFactory.stringToObject(JSONFactory.read(file).toString(), javaType);
            }else {
                oneFileServers = YamlUtil.toEntity(file.getAbsolutePath(), constructor);
            }
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

    /**
     * 首先尝试从swagger加载配置文件，但是兼容有问题，所以不建议
     */
    private static List<Server> swagger(String folderName){
        List<File> swaggerFiles = new ArrayList<>();
        FileUtil.collect(new File(folderName), swaggerFiles, new String[]{"swagger"});
        List<Server> servers = new ArrayList<>();
        swaggerFiles.forEach(swagger -> {
            JsonNode swaggerNode = JSONFactory.read(swagger);
            String host = swaggerNode.path("info").asText("host");
            JsonNode paths = swaggerNode.path("paths");
            Server server = new Server();
            server.setServerName(host);
            for (Iterator<Map.Entry<String, JsonNode>> it = paths.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> pathEntry = it.next();
                String uriPath = pathEntry.getKey();
                List<Uri> uris = new ArrayList<>();
                for (Iterator<Map.Entry<String, JsonNode>> iter = pathEntry.getValue().fields(); iter.hasNext(); ) {
                    Map.Entry<String, JsonNode> methodEntry = iter.next();
                    String method = methodEntry.getKey();
                    Uri uri = new Uri();
                    uri.setUri(uriPath);
                    uri.setMethod(method);
                    uri.setHost(host);
                    uris.add(uri);
                }
                server.setUris(uris);
            }
            servers.add(server);
        });
        return servers;
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
