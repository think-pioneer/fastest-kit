package xyz.think.fastest.core.internal.enhance.methodhelper;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import xyz.think.fastest.common.exceptions.FileException;
import xyz.think.fastest.http.ReadApiConfig;
import xyz.think.fastest.logger.FastestLogger;
import xyz.think.fastest.logger.FastestLoggerFactory;
import xyz.think.fastest.utils.dates.DateTime;
import xyz.think.fastest.utils.dates.DateUtil;
import xyz.think.fastest.utils.files.FileUtil;
import xyz.think.fastest.utils.files.PropertyUtil;
import xyz.think.fastest.utils.string.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 会记录RestTemp的http信息
 * 将记录的http信息写入到文件
 * @Date: 2021/10/29
 */
public enum RestTempCache {
    INSTANCE;
    private static final FastestLogger logger = FastestLoggerFactory.getLogger(RestTempCache.class);
    private final String CLASS_TAG = String.format("!!%s", ReadApiConfig.Server.class.getName());
    private final ConcurrentLinkedQueue<ReadApiConfig.Server> cacheOfCache = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, ReadApiConfig.Server> cache = new ConcurrentHashMap<>();
    private volatile Boolean lock;
    private Timer cleanTimer;
    private Timer saveTimer;

    RestTempCache(){
        boolean timing = PropertyUtil.getOrDefault("fastest.rest.temp.api.save.timing", true);
        if (timing) {
            long period = PropertyUtil.getOrDefault("fastest.rest.temp.api.save.period", 60*1000);
            // 定时把http信息写入到文件
            this.saveTimer = new Timer(true);
            this.saveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    innerSave();
                }
            }, 10000, period);
            // 定期将cacheOfCache中的元素消费到cache中。
            this.cleanTimer = new Timer(true);
            this.cleanTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    INSTANCE.consumer();
                }
            }, 10000, 30*1000);
        }
    }

    synchronized void innerAdd(ReadApiConfig.Server server){
        if(Objects.nonNull(server)){
            cacheOfCache.add(server);
        }
    }

    List<ReadApiConfig.Server> innerGetAllApi(){
        return new ArrayList<>(cache.values());
    }
    boolean innerIsEmpty() {
        return this.cache.isEmpty();
    }

    synchronized void write(OutputStreamWriter writer) throws IOException {
        lock = true;
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        options.setPrettyFlow(false);
        Yaml yaml = new Yaml(options);
        StringWriter sw = new StringWriter();
        yaml.dumpAll(cache.values().iterator(), sw);
        writer.write(String.valueOf(sw).replace(CLASS_TAG, "").trim());
        cache.clear();
        lock = false;
    }

    private synchronized void consumer() {
        if (lock) {
            return;
        }
        for (;;) {
            ReadApiConfig.Server server = cacheOfCache.poll();
            if (server == null) {
                break;
            }
            String key = server.getHost();
            if(cache.containsKey(key)){
                cache.get(key).getUris().addAll(server.getUris());
            }else{
                cache.put(key, server);
            }
        }
    }

    public static void add(ReadApiConfig.Server server){
        RestTempCache.INSTANCE.innerAdd(server);
    }

    public static List<ReadApiConfig.Server> getAllApi() {
        return RestTempCache.INSTANCE.innerGetAllApi();
    }

    public static boolean isEmpty() {
        return RestTempCache.INSTANCE.innerIsEmpty();
    }

    private void innerSave() {
        if (innerIsEmpty()) {
            return;
        }
        String defaultPath = PropertyUtil.getProperty("fastest.rest.temp.api.path");
        if(Objects.isNull(defaultPath)){
            defaultPath = StringUtils.format("apiconfig_custom/APIConfTemp_{0}_{1}.yaml", DateTime.newInstance(new Date(), DateUtil.FORMAT_D).string(), new Random().nextInt(10000));
        }
        File file = FileUtil.createFile(defaultPath);
        if(Objects.isNull(file) || !file.exists()){
            throw new FileException("create rest temp api json file of fail:" + defaultPath);
        }
        OutputStreamWriter writer = null;
        try{
            writer = new OutputStreamWriter(new FileOutputStream(file));
            write(writer);
        }catch (IOException e){
            throw new FileException("write rest temp api json file of fail:" + file.getAbsolutePath());
        }finally {
            if(Objects.nonNull(writer)){
                try{
                    writer.flush();
                    writer.close();
                } catch (IOException e){
                    logger.error("close rest temp api json file fail");
                }
            }
        }
    }


    /**
     * 框架shutdown时调用
     * 先取消定时任务，在手动消费堆积的任务。
     */
    public static void save() {
        RestTempCache.INSTANCE.saveTimer.cancel();
        RestTempCache.INSTANCE.cleanTimer.cancel();
        RestTempCache.INSTANCE.consumer();
        RestTempCache.INSTANCE.innerSave();
    }

}
