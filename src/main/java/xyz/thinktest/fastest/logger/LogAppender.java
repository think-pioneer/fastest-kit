package xyz.thinktest.fastest.logger;

import xyz.thinktest.fastest.utils.files.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

enum LogAppender {
    INSTANCE;

    private final Map<LogLevel, BufferedWriter> bufferWriterMap;

    LogAppender(){
        this.bufferWriterMap = new HashMap<>();
        this.buildWriterMap();
    }

    private BufferedWriter buildWriter(String path){
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

    private void buildWriterMap(){
        String path = "logs/fastestlogger_"+"{type}_"+DateUtil.INSTANCE.getDate()+".log";
        for(LogLevel level: LogLevel.values()){
            String _path = path.replace("{type}", level.type.toLowerCase());
            BufferedWriter bw = buildWriter(_path);
            if(Objects.nonNull(bw)){
                bufferWriterMap.put(level, bw);
            }
        }
    }

    public Map<LogLevel, BufferedWriter> getBufferWriterMap() {
        return bufferWriterMap;
    }
}
