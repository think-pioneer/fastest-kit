package xyz.thinktest.fastest.core.internal.enhance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.thinktest.fastest.common.exceptions.FileException;
import xyz.thinktest.fastest.core.internal.enhance.methodhelper.RestTempWrite;
import xyz.thinktest.fastest.utils.dates.DateUtil;
import xyz.thinktest.fastest.utils.files.FileUtil;
import xyz.thinktest.fastest.utils.files.PropertyUtil;
import xyz.thinktest.fastest.utils.ObjectUtil;

import java.io.*;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * @Date: 2021/10/31
 */
public final class ShutdownHook {
    private static final Logger logger = LogManager.getLogger(ShutdownHook.class);

    private ShutdownHook(){}

    public static void writeApiTempJson(){
        if(RestTempWrite.getAllApi().isEmpty()){
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String defaultPath = PropertyUtil.getProperty("rest.temp.api");
            if(Objects.isNull(defaultPath)){
                defaultPath = ObjectUtil.format("apiconfig_custom/APIConfTemp_{}_{}.yaml", DateUtil.dateToString(new Date(), "yyyy_MM_dd_HH_mm_ss"), new Random().nextInt(10000));
            }
            File file = FileUtil.createFile(defaultPath);
            if(Objects.isNull(file) || !file.exists()){
                throw new FileException("create rest temp api json file of fail:" + defaultPath);
            }
            OutputStreamWriter writer = null;
            try{
                writer = new OutputStreamWriter(new FileOutputStream(file));
                RestTempWrite.save(writer);
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
        }));
    }
}
