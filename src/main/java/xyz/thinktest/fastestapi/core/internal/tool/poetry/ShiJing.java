package xyz.thinktest.fastestapi.core.internal.tool.poetry;

import com.fasterxml.jackson.databind.JsonNode;
import xyz.thinktest.fastestapi.common.json.JSONFactory;
import xyz.thinktest.fastestapi.core.internal.tool.poetry.entities.ShiJingEntity;

import java.util.List;
import java.util.Random;

/**
 * @author: aruba
 * @date: 2022-01-31
 */
public class ShiJing implements Poetry{
    private final List<ShiJingEntity> contents;

    public ShiJing(){
        JsonNode text = JSONFactory.read(ShiJing.class.getClassLoader().getResourceAsStream("poetry/shijing/shijing.json"));
        contents = JSONFactory.stringToObject(text.toString(), List.class, ShiJingEntity.class);
    }

    @Override
    public String content() {
        ShiJingEntity entity = rangeShiJing();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n《").append(entity.getTitle()).append("》")
                .append("  --").append(entity.getChapter()).append("·").append(entity.getSection()).append("\r\n\r\n");
        for(String s:entity.getContent()){
            sb.append(s).append("\r\n");
        }
        return sb.toString();
    }

    private ShiJingEntity rangeShiJing(){
        Random random = new Random();
        return contents.get(random.nextInt(contents.size()));
    }
}
