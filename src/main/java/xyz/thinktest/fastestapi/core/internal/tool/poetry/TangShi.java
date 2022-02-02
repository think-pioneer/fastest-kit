package xyz.thinktest.fastestapi.core.internal.tool.poetry;

import xyz.thinktest.fastestapi.common.json.JSONFactory;
import xyz.thinktest.fastestapi.core.internal.tool.poetry.entities.TangShiEntity;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * @author: aruba
 * @date: 2022-02-01
 */
public class TangShi implements Poetry{
    private List<TangShiEntity> tangShiEntity;
    public TangShi(){
        Random rand = new Random();
        String[] prefixes = {"poet.tang", "poet.song"};
        String namePrefix = prefixes[rand.nextInt(prefixes.length)];
        int num = 0;
        if(namePrefix.endsWith("tang")){
            num = rand.nextInt(58) * 1000;
        }else if(namePrefix.endsWith("song")){
            num = rand.nextInt(255) * 1000;
        }
        String filename = String.format("%s.%d.json", namePrefix, num);
        InputStream is = TangShi.class.getClassLoader().getResourceAsStream("poetry/poet/" + filename);
        tangShiEntity = JSONFactory.stringToObject(JSONFactory.read(is).toString(), List.class, TangShiEntity.class);
    }

    @Override
    public String show() {
        TangShiEntity entity = rangeTangShi();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n").append(entity.getTitle()).append("·").append(entity.getAuthor()).append("\r\n");
        for(String str:entity.getParagraphs()){
            sb.append(str).append("\r\n");
        }
        return sb.toString();
    }

    private TangShiEntity rangeTangShi(){
        return tangShiEntity.get(new Random().nextInt(tangShiEntity.size()));
    }
}
