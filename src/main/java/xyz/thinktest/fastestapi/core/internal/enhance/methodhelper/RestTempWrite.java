package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import xyz.thinktest.fastestapi.core.rest.http.metadata.ReadApiConfig;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Date: 2021/10/29
 */
public final class RestTempWrite {
    private static final ConcurrentLinkedQueue<ReadApiConfig.Server> list = new ConcurrentLinkedQueue<>();
    private static final ConcurrentHashMap<String, ReadApiConfig.Server> map = new ConcurrentHashMap<>();

    private RestTempWrite(){}

    public static synchronized void add(ReadApiConfig.Server server){
        if(Objects.nonNull(server)){
            String key = server.getHost();
            if(map.containsKey(key)){
                map.get(key).getUris().addAll(server.getUris());
            }else{
                map.put(key, server);
            }
        }
    }

    public static ConcurrentLinkedQueue<ReadApiConfig.Server> getAllApi(){
        list.addAll(map.values());
        return list;
    }

    public static synchronized void save(OutputStreamWriter writer) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        options.setPrettyFlow(false);
        Yaml yaml = new Yaml(options);
        StringWriter sw = new StringWriter();
        yaml.dumpAll(list.iterator(), sw);
        writer.write(String.valueOf(sw).replace("!!xyz.thinktest.fastestapi.core.rest.http.metadata.ReadApiConfig$Server", "").trim());
    }
}
