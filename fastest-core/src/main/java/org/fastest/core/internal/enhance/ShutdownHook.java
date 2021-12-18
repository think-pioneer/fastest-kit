package org.fastest.core.internal.enhance;

import org.fastest.common.exceptions.FileException;
import org.fastest.core.internal.enhance.methodhelper.RestTempWrite;
import org.fastest.utils.FileUtil;
import org.fastest.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

/**
 * @Date: 2021/10/31
 */
public final class ShutdownHook {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private ShutdownHook(){}

    public static void writeApiTempJson(){
        if(RestTempWrite.getAllApi().isEmpty()){
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String defaultPath = PropertyUtil.getProperty("rest.temp.api");
            if(Objects.isNull(defaultPath)){
                defaultPath = "conf/APIConfTemp.json";
            }
            File file = FileUtil.createFile(defaultPath);
            if(Objects.isNull(file)){
                throw new FileException("create rest temp api json file of fail:" + defaultPath);
            }
            OutputStreamWriter writer = null;
            try{
                writer = new OutputStreamWriter(new FileOutputStream(file));
                writer.write(RestTempWrite.pretty());
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
