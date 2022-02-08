package xyz.thinktest.fastestapi.core.internal.enhance.methodhelper;

import xyz.thinktest.fastestapi.common.exceptions.FileException;
import xyz.thinktest.fastestapi.core.enhance.ShutdownHook;
import xyz.thinktest.fastestapi.logger.FastestLogger;
import xyz.thinktest.fastestapi.logger.FastestLoggerFactory;
import xyz.thinktest.fastestapi.utils.ObjectUtil;
import xyz.thinktest.fastestapi.utils.dates.DateTime;
import xyz.thinktest.fastestapi.utils.dates.DateUtil;
import xyz.thinktest.fastestapi.utils.files.FileUtil;
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * @author: aruba
 *
 * @date: 2022-01-28
 */
public class RestWriteHook extends ShutdownHook {
    private static final FastestLogger logger = FastestLoggerFactory.getLogger(RestWriteHook.class);
    @Override
    public void run() {
        String defaultPath = PropertyUtil.getProperty("fastest.rest.temp.api");
        if(Objects.isNull(defaultPath)){
            defaultPath = ObjectUtil.format("apiconfig_custom/APIConfTemp_{}_{}.yaml", DateTime.newInstance(new Date(), DateUtil.FORMAT_D).string(), new Random().nextInt(10000));
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
    }
}
