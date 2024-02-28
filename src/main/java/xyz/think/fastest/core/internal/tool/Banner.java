package xyz.think.fastest.core.internal.tool;


import com.google.common.io.ByteStreams;
import xyz.think.fastest.utils.color.ColorPrint;
import xyz.think.fastest.utils.files.PropertyUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: aruba
 * @date: 2022-01-29
 */
public enum Banner {
    INSTANCE;
    public void print() {
        try {
            InputStream is = Banner.class.getClassLoader().getResourceAsStream("banner.txt");
            String text = new String(ByteStreams.toByteArray(is));
            for (String line : text.trim().split("\r\n")) {
                parseLine(line);
            }
        }catch (Exception e){
            String text = "\n" +
                    " _______  _______  _______ _________ _______  _______ _________\n" +
                    "(  ____ \\(  ___  )(  ____ \\\\__   __/(  ____ \\(  ____ \\\\__   __/\n" +
                    "| (    \\/| (   ) || (    \\/   ) (   | (    \\/| (    \\/   ) (   \n" +
                    "| (__    | (___) || (_____    | |   | (__    | (_____    | |   \n" +
                    "|  __)   |  ___  |(_____  )   | |   |  __)   (_____  )   | |   \n" +
                    "| (      | (   ) |      ) |   | |   | (            ) |   | |   \n" +
                    "| )      | )   ( |/\\____) |   | |   | (____/\\/\\____) |   | |   \n" +
                    "|/       |/     \\|\\_______)   )_(   (_______/\\_______)   )_(   \n" +
                    "                                                               \n";
            ColorPrint.CYAN.println(text);
        }
        ColorPrint.GREEN.println("\r\n:: Fastest ::      (" + PropertyUtil.getProperty("fastest.api.version") + ")");
    }

    private void parseLine(String line){
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(line);
        List<String> colorTypes = new ArrayList<>();
        List<String> colorPlaces = new ArrayList<>();
        while(matcher.find()){
            colorPlaces.add(matcher.group(0));
            colorTypes.add(matcher.group(1));
        }
        List<Entity> contents = new ArrayList<>();
        for(int i = colorPlaces.size() - 1; i >= 0; i--){
            String[] messages = line.split(colorPlaces.get(i).replace("$", "\\$").replace("{", "\\{").replace("}", "\\}"));
            if(messages.length <= 1){
                break;
            }
            contents.add(new Entity(colorTypes.get(i), messages[messages.length - 1]));
            String[] tmp = Arrays.copyOf(messages, messages.length - 1);
            line = String.join(colorPlaces.get(i), tmp);
        }
        Collections.reverse(contents);
        for(Entity entity:contents){
            entity.show();
        }
        System.out.println();

    }

    static class Entity{
        private final String colorType;
        private final String content;

        public Entity(String colorType, String content) {
            this.colorType = colorType;
            this.content = content;
        }

        public void show(){
            ColorPrint.color(colorType).print(content);
        }
    }
}
