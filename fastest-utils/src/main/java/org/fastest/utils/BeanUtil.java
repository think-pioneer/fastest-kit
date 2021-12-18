package org.fastest.utils;

/**
 * @Date: 2021/12/5
 */


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.fastest.common.exceptions.FastestBasicException;
import org.fastest.common.exceptions.FileException;
import org.fastest.common.json.JSONFactory;

import java.io.*;
import java.util.Map;

public class BeanUtil {

    /**
     * create bean file from map
     */
    public static void createBeanForFile(String packagePath, String beanName, Map<String, Object> map){
        if(StringUtils.isEmpty(packagePath)){
            throw new FileException("not found package:" + packagePath);
        }
        if(StringUtils.isEmpty(beanName)){
            throw new FileException("file name is empty");
        }
        beanName = beanName.substring(0, 1).toUpperCase();
        String fileName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, beanName);
        File file = new File(FileUtil.PROJECT_ROOT, "src/main/java/"+packagePath.replace(".", "/") + "/" + fileName + ".java");
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packagePath).append(";\n\n");
        sb.append("public class ").append(fileName).append("{\n");
        for(Map.Entry<String, Object> entry:map.entrySet()){
            sb.append("\t").append("private ").append(entry.getValue().getClass().getSimpleName()).append(" ").append(entry.getKey()).append(";\n");
        }
        for(Map.Entry<String, Object> entry:map.entrySet()){
            String fieldName = entry.getKey();
            String fieldType = entry.getValue().getClass().getSimpleName();
            sb.append("\t").append("public ")
                    .append(fieldType)
                    .append(" get").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).append("() {\n")
                    .append("\t\treturn ").append(fieldName).append(";\n\t}\n\n");
            sb.append("\t").append("public void set")
                    .append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).append("(")
                    .append(fieldType).append(" ").append(fieldName).append(") {\n").append("\t\t")
                    .append("this.").append(fieldName).append(" = ").append(fieldName).append(";\n\t}\n\n");
        }
        sb.append("}");
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(sb.toString());
            bw.close();
            System.out.println("create bean file success:"+file.getAbsolutePath());
        }catch (Exception e){
            throw new FileException("creat bean file fail", e);
        }
    }

    public static void createBeanForFile(String packagePath, String beanName, String content){
        createBeanForFile(packagePath, beanName, JSONFactory.stringToObject(content, new TypeReference<Map<String, Object>>() {}));
    }

    public static <T> T deepCopy(T src){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(src);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            T deepObj = (T) ois.readObject();
            ois.close();
            bais.close();
            oos.close();
            baos.close();
            return deepObj;
        }catch (IOException | ClassNotFoundException e){
            throw new FastestBasicException("deep copy object fail", e);
        }
    }
}
