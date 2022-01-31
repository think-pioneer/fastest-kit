package xyz.thinktest.fastestapi.core.internal.tool.poetry;

import com.fasterxml.jackson.databind.JsonNode;
import xyz.thinktest.fastestapi.common.json.JSONFactory;
import xyz.thinktest.fastestapi.core.internal.tool.poetry.entities.ShijingEntity;
import xyz.thinktest.fastestapi.utils.ColorPrint;

import java.util.List;
import java.util.Random;

/**
 * @author: aruba
 * @date: 2022-01-31
 */
public class Shijing implements Poetry{
    private final List<ShijingEntity> contents;

    public Shijing(){
        JsonNode text = JSONFactory.read(Shijing.class.getClassLoader().getResourceAsStream("poetry/shijing.json"));
        contents = JSONFactory.stringToObject(text.toString(), List.class, ShijingEntity.class);
    }

    @Override
    public void show() {
        ShijingEntity entity = rangeShijing();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n").append(entity.getChapter()).append("·").append(entity.getSection()).append("·").append(entity.getTitle()).append("\r\n");
        for(String s:entity.getContent()){
            sb.append(s).append("\r\n");
        }
        ColorPrint.CYAN.println(sb.toString());
    }

    private ShijingEntity rangeShijing(){
        Random random = new Random();
        return contents.get(random.nextInt(contents.size()));
    }
}
