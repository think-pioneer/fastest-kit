package xyz.thinktest.fastestapi.logger;

import xyz.thinktest.fastestapi.utils.ColorPrint;
import xyz.thinktest.fastestapi.common.exceptions.FileException;
import xyz.thinktest.fastestapi.utils.dates.DateTime;
import xyz.thinktest.fastestapi.utils.dates.DateUtil;
import xyz.thinktest.fastestapi.utils.files.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

enum LogAppender {
    INSTANCE;

    private final Map<LogLevel, BufferedWriter> bufferWriterMap;
    private final String yesterday;

    LogAppender(){
        this.bufferWriterMap = new HashMap<>();
        this.yesterday = DateUtil.DAY.calculate(new Date(),-1).toDateTime(DateUtil.FORMAT_A).string();
        this.buildZip();
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

    private void buildZip(){
        String zipFileName = String.format("logs/fastestlog-%s.zip", yesterday);
        File zipFile = new File(FileUtil.getProjectRoot(), zipFileName);
        FileInputStream fis;
        BufferedInputStream bis = null;
        FileOutputStream fos;
        ZipOutputStream zos = null;

        try {
            File sourceFilePath = new File(FileUtil.getProjectRoot(), "logs");
            if(sourceFilePath.exists()){
                List<File> sourceFiles = new ArrayList<>();
                FileUtil.collect(sourceFilePath, sourceFiles, new String[]{".log"});
                sourceFiles = filterByCreateTime(sourceFiles);
                if(sourceFiles.size() > 0){
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    byte[] bufs = new byte[1024*10];
                    for (File sourceFile : sourceFiles) {
                        //创建ZIP实体，并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(sourceFile);
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                        bis.close();
                        fis.close();
                        if(!sourceFile.delete()){
                            ColorPrint.YELLOW.println("删除log文件失败：" + sourceFile.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (IOException e) {
            ColorPrint.RED.println("Rolling log for zip fail.IOException");
            e.printStackTrace();
            throw new FileException(e);
        } finally{
            //关闭流
            try {
                if(null != bis) bis.close();
                if(null != zos) zos.close();
            } catch (IOException e) {
                ColorPrint.RED.println("Rolling log for zip fail.Close IOException");
                e.printStackTrace();
            }
        }
    }

    private List<File> filterByCreateTime(List<File> fileList){
        return fileList.stream().filter(file -> {
            try {
                Path path = file.toPath();
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                Instant instant = attr.creationTime().toInstant();
                Date fileTime = DateTime.newInstance(new Date(instant.toEpochMilli()), DateUtil.FORMAT_A).toDate();
                Date actualTime = DateUtil.DAY.calculate(new Date(), 0).toDateTime(DateUtil.FORMAT_A).toDate();
                return DateUtil.DAY.diff(fileTime, actualTime) > 0;
            }catch (IOException e){
                ColorPrint.RED.println("read file error");
                e.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());
    }

    private void buildWriterMap(){
        String path = "logs/fastestlog-"+"{type}-"+ DateTime.newInstance(new Date(), DateUtil.FORMAT_A).string() +".log";
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
