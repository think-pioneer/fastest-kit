package org.fastest.http.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.fastest.common.json.JSONFactory;
import org.fastest.utils.FileUtil;
import org.fastest.utils.YamlUtil;

import java.io.File;
import java.util.*;

/**
 * @Date: 2020/10/26
 */
public final class ReadApiConfig {
    private static final File root = FileUtil.getResourcesPath();
    private static String folderName = "apiconfig";
    private static final List<Server> servers = init();
    private static Map<String, Map<String, Api>> serverMap;
    private static Map<String, Map<String, Map<String, Api>>> serverMapByFile;

    private ReadApiConfig(){}

    public static String printTemplate(){
        String template = "[\n" +
                "  {\"serverName\": \"user_system\", \"desc\": \"用户系统服务\", \"host\": \"http://user.testdemo.com\",\n" +
                "    \"apis\": [\n" +
                "      {\"apiName\":\"get_userinfo_by_id\",\"api\": \"/api/userinfo\", \"method\": \"get\", \"desc\": \"通过id获取用户信息\"},\n" +
                "      {\"apiName\":\"get_userinfo_by_id_restful\",\"api\": \"/api/{id}/userinfo\", \"method\": \"get\", \"desc\": \"通过id获取用户信息(restful)\"}\n" +
                "    ]\n" +
                "  },\n" +
                "\n" +
                "  {\"serverName\": \"manager_system\", \"desc\": \"管理员系统服务\", \"host\": \"http://manager.testdemo.com\",\n" +
                "    \"apis\": [\n" +
                "      {\"apiName\":\"get_manager_info_by_id\",\"api\": \"/api/managerInfo\", \"method\": \"get\", \"desc\": \"通过id获取管理员信息\"},\n" +
                "      {\"apiName\":\"get_manager_info_by_id_restful\",\"api\": \"/api/{id}/managerInfo\", \"method\": \"get\", \"desc\": \"通过id获取管理员信息(restful)\"}\n" +
                "    ]\n" +
                "  }\n" +
                "]";
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
    public static Api getApi(String serverName, String apiName){
        return getApi(serverName, apiName, null);
    }

    /**
     * get api config api node
     * @param serverName server name
     * @param apiName api name
     * @param file apiconfig file path
     * @return api object
     */
    public static Api getApi(String serverName, String apiName, String file){
        if(Objects.isNull(serverName) || Objects.isNull(apiName)) return null;
        try {
            Map<String, Map<String, Api>> _serverMap;
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
     * initialization api config
     * @return api config object
     */
    private static List<Server> init(){
        String apiConfName = YamlUtil.getString("apiconfig.folderpath");
        if(Objects.nonNull(apiConfName)){
            folderName = apiConfName;
        }
        List<Server> allFileServers = new ArrayList<>();
        serverMap = new HashMap<>();
        serverMapByFile = new HashMap<>();
        List<File> fileList = new ArrayList<>();
        FileUtil.collect(FileUtil.createFolder(root, folderName), fileList, new String[]{".json"});
        fileList.forEach(file -> {
            JsonNode rootNode = JSONFactory.read(FileUtil.read(file));
            List<Server> oneFileServers = JSONFactory.stringToObject(rootNode.toString(), ArrayList.class, Server.class);
            Map<String, Map<String, Api>> tmpServerMap = new HashMap<>();
            for(Server server:oneFileServers){
                Map<String, Api> apiMap = new HashMap<>();
                for(Api api:server.apis){
                    api.setHost(server.host);
                    api.setUrl(server.host+api.api);
                    apiMap.put(server.serverName+api.apiName, api);
                }
                tmpServerMap.put(server.serverName, apiMap);
                serverMap.put(server.serverName, apiMap);
            }
            serverMapByFile.put(file.getAbsolutePath(), tmpServerMap);
            allFileServers.addAll(oneFileServers);
        });
        return allFileServers;
    }

    @Data
    public static class Server{
        private String serverName;
        private String desc;
        private String host;
        private List<Api> apis;
    }

    @Data
    public static class Api{
        private String apiName;
        private String api;
        private String host;
        private String method;
        private String desc;
        private String url;
    }
}
