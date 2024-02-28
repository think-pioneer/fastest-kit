package xyz.think.fastest.utils.files;

import xyz.think.fastest.common.exceptions.FileException;
import xyz.think.fastest.utils.string.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2020/10/21
 */
public enum  FileUtil {
    INSTANCE;

    private final File resourcePath;
    FileUtil(){
        try {
            URL url = FileUtil.class.getResource("/");
            if (Objects.isNull(url)) {
                //jar包模式
                String path = FileUtil.class.getResource("").getPath();
                path = path.replace("file:/", "").split("!")[0];
                path = URLDecoder.decode(path, "UTF-8");
                resourcePath = new File(new File(path).getParent());
            } else {
                //非jar模式
                resourcePath = new File(new File(url.getFile()).getParentFile(), "classes");
            }
        }catch (UnsupportedEncodingException e){
            throw new FileException("initialization path fail", e);
        }
    }
    public File projectRootPath(){
        return new File(System.getProperty("user.dir"));
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
                throw new FileException(StringUtils.format("create path fail:{0}", path), e);
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
                throw new FileException(StringUtils.format("create path fail:{0}", file.getAbsolutePath()), e);
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
            file = new File(getClassPath(), path);
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
        if(path.isFile()){
            if(isSuffix(path, suffixes)){
                fileList.add(path);
            }
            return;
        }
        File[] files = path.listFiles();
        if(Objects.nonNull(files)){
            for(File file:files){
                collectInternal(file, fileList, suffixes);
            }
        }
    }

    public void collectInternal(File path, List<File> fileList){
        if(!path.exists()){
            return;
        }
        if(path.isFile()){
            fileList.add(path);
            return;
        }
        File[] files = path.listFiles();
        if(Objects.nonNull(files)){
            for(File file:files){
                collectInternal(file, fileList);
            }
        }
    }

    /**
     * create file
     * @param path file  path
     * @return if success return file,else return null;
     */
    public static File createFile(String path){
        return FileUtil.INSTANCE.createFileInternal(path);
    }

    /**
     * create file
     * @param path file  path
     * @return if success return file,else return null;
     */
    public static File createFile(File file, String path){
        return FileUtil.INSTANCE.createFileInternal(new File(file, path));
    }

    /**
     * create folder
     * @param path folder path
     * @return if success return file,else return null;
     */
    public static File createFolder(String path){
        return FileUtil.INSTANCE.createFolderInternal(path);
    }

    /**
     * create folder
     * @param path folder path
     * @return if success return file,else return null;
     */
    public static File createFolder(File file, String path){

        return FileUtil.INSTANCE.createFolderInternal(new File(file, path));
    }

    /**
     * read file to InputStream
     * @param path file path
     * @return InputStream
     */
    public static InputStream read(String path){
        return FileUtil.INSTANCE.readInternal(path);
    }

    /**
     * read file to InputStream
     * @param path file path
     * @return InputStream
     */
    public static InputStream read(File path){
        return FileUtil.INSTANCE.readInternal(path.getAbsolutePath());
    }

    /**
     * Collect files under the path, including subdirectory
     * @param path root path
     * @param fileList file collection container
     * @param suffixes filter file by suffix
     */
    public static void collect(File path, List<File> fileList, String[] suffixes){
        FileUtil.INSTANCE.collectInternal(path, fileList, suffixes);
    }

    /**
     * Collect files under the path, including subdirectory
     * @param path root path
     * @param fileList file collection container
     */
    public static void collect(File path, List<File> fileList){
        FileUtil.INSTANCE.collectInternal(path, fileList);
    }

    public static File getClassPath(){
        return FileUtil.INSTANCE.resourcePath;
    }

    public static File getProjectRoot(){
        return FileUtil.INSTANCE.projectRootPath();
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
