package org.fastest.utils.files;

import org.fastest.common.exceptions.FileException;
import org.fastest.utils.ObjectUtil;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2020/10/21
 */
public final class FileUtil {
    public static final File PROJECT_ROOT = new File(System.getProperty("user.dir"));
    private File resourcePath;
    private volatile static FileUtil instance = null;

    public static FileUtil getInstance(){
        if(Objects.isNull(FileUtil.instance)){
            synchronized (FileUtil.class){
                if(Objects.isNull(FileUtil.instance)){
                    instance = new FileUtil();
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init(){
        URL url = FileUtil.class.getResource("/");
        File file;
        if(Objects.isNull(url)){
            //jar包模式
            file = new File(resourcePath, "resources");
        }else{
            //非jar模式
            file = new File(new File(url.getFile()).getParentFile(), "classes");
        }
        resourcePath = file;
    }

    private File createFileInternal(String path){
        File file = new File(path);
        if(!file.isAbsolute()){
            file = new File(resourcePath, path);
        }
        if(!file.exists()){
            try {
                createFolder(file.getParent());
                return file.createNewFile() ? file : null;
            }catch (IOException e){
                throw new FileException(ObjectUtil.format("create path fail:{}", path), e);
            }
        }
        return file;
    }

    private File createFileInternal(File file){
        if(!file.isAbsolute()){
            file = new File(resourcePath, file.getPath());
        }
        if(!file.exists()){
            try {
                createFolder(file.getParent());
                return file.createNewFile() ? file : null;
            }catch (IOException e){
                throw new FileException(ObjectUtil.format("create path fail:{}", file.getAbsolutePath()), e);
            }
        }
        return file;
    }

    private File createFolderInternal(String path){
        return createFolderInternal(new File(path));
    }

    private File createFolderInternal(File file){
        if(!file.isAbsolute()){
            file = new File(resourcePath, file.getPath());
        }
        if(!file.exists()){
            return file.mkdirs() ? file : null;
        }
        return file;
    }

    private InputStream readInternal(String path){
        File file = new File(path);
        if(!file.isAbsolute()){
            file = new File(getResourcesPath(), path);
        }
        try {
            return new FileInputStream(file);
        }catch (FileNotFoundException e){
            throw new FileException(e.getMessage(), e.getCause());
        }
    }

    public void collectInternal(File path, List<File> fileList, String[] suffixes){
        if(!path.exists()){
            return;
        }
        if(isSuffix(path, suffixes)){
            fileList.add(path);
        }
        File[] files = path.listFiles();
        if(Objects.nonNull(files)){
            for(File file:files){
                collect(file, fileList, suffixes);
            }
        }
    }

    public void collectInternal(File path, List<File> fileList){
        if(!path.exists()){
            return;
        }
        if(path.isFile()){
            fileList.add(path);
        }
        File[] files = path.listFiles();
        if(Objects.nonNull(files)){
            for(File file:files){
                collect(file, fileList);
            }
        }
    }

    /**
     * create file
     * @param path file  path
     * @return if success return file,else return null;
     */
    public static File createFile(String path){
        return getInstance().createFileInternal(path);
    }

    /**
     * create file
     * @param path file  path
     * @return if success return file,else return null;
     */
    public static File createFile(File file, String path){
        return getInstance().createFileInternal(new File(file, path));
    }

    /**
     * create folder
     * @param path folder path
     * @return if success return file,else return null;
     */
    public static File createFolder(String path){
        return getInstance().createFolderInternal(path);
    }

    /**
     * create folder
     * @param path folder path
     * @return if success return file,else return null;
     */
    public static File createFolder(File file, String path){

        return getInstance().createFolderInternal(new File(file, path));
    }

    /**
     * read file to InputStream
     * @param path file path
     * @return InputStream
     */
    public static InputStream read(String path){
        return getInstance().readInternal(path);
    }

    /**
     * read file to InputStream
     * @param path file path
     * @return InputStream
     */
    public static InputStream read(File path){
        return getInstance().readInternal(path.getAbsolutePath());
    }

    /**
     * Collect files under the path, including subdirectory
     * @param path root path
     * @param fileList file collection container
     * @param suffixes filter file by suffix
     */
    public static void collect(File path, List<File> fileList, String[] suffixes){
        getInstance().collectInternal(path, fileList, suffixes);
    }

    /**
     * Collect files under the path, including subdirectory
     * @param path root path
     * @param fileList file collection container
     */
    public static void collect(File path, List<File> fileList){
        getInstance().collectInternal(path, fileList);
    }

    public static File getResourcesPath(){
        return getInstance().resourcePath;
    }

    public static boolean isSuffix(File file, String[] suffixes){
        return isSuffix(new ArrayList<File>(){{add(file);}}, suffixes);
    }

    public static boolean isSuffix(File file, String suffix){
        return file.getAbsolutePath().endsWith(suffix);
    }

    public static boolean isSuffix(List<File> fileList, String suffix){
        for(File file:fileList){
            if(file.getAbsolutePath().endsWith(suffix)){
                return true;
            }
        }
        return false;
    }

    public static boolean isSuffix(List<File> fileList, String[] suffixes){
        for(File file:fileList) {
            for (String suffix : suffixes) {
                if (file.isFile() && file.getAbsolutePath().endsWith(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }
}
