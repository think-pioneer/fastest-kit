package xyz.thinktest.fastest.logger;

import xyz.thinktest.fastest.utils.files.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum FastestAppender {
    INSTANCE;

    public BufferedWriter getWriter(String path){
        File file;
        if(FileUtil.getClassPath().equals(FileUtil.getProjectRoot())){
            file = FileUtil.createFile(FileUtil.getClassPath(), path);
        }else{
            file = FileUtil.createFile(FileUtil.getProjectRoot(), path);
        }
        try {
            FileWriter fw;
            if(!file.exists()){
                fw = new FileWriter(file);
                fw.close();
            }
            return new BufferedWriter(new FileWriter(file, true));
        }catch (IOException e){
            System.err.println("read file "+file.getAbsolutePath()+" error");
            return null;
        }
    }

    public Map<FastestLogger.LogLevel, BufferedWriter> getWriterList(){
        String path = "logs/fastestlogger_"+"{type}_"+DateUtil.INSTANCE.getDate()+".log";
        Map<FastestLogger.LogLevel, BufferedWriter> bwMap = new HashMap<>();
        for(FastestLogger.LogLevel level: FastestLogger.LogLevel.values()){
            String _path = path.replace("{type}", level.type.toLowerCase());
            BufferedWriter bw = getWriter(_path);
            if(Objects.nonNull(bw)){
                bwMap.put(level, bw);
            }
        }
        return bwMap;
    }
}
