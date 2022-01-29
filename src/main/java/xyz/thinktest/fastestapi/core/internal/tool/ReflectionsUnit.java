package xyz.thinktest.fastestapi.core.internal.tool;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import xyz.thinktest.fastestapi.utils.files.PropertyUtil;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: aruba
 * @date: 2022-01-28
 */
public enum ReflectionsUnit {
    INSTANCE;
    public final Reflections reflections;

    ReflectionsUnit(){
        try {
            String root = PropertyUtil.getProperty("java.class.path");
            List<URL> urls = new ArrayList<>();
            for (String path : root.split(File.pathSeparator)) {
                urls.add(new File(path).toURI().toURL());
            }
            this.reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(urls).setScanners(Scanners.SubTypes, Scanners.MethodsAnnotated, Scanners.FieldsAnnotated)
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
