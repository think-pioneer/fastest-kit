package xyz.thinktest.fastestapi.core.internal.tool.poetry;

import xyz.thinktest.fastestapi.common.json.JSONFactory;
import xyz.thinktest.fastestapi.core.internal.tool.poetry.entities.SongCiEntity;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * @author: aruba
 * @date: 2022-02-01
 */
public class SongCi implements Poetry{
    private final List<SongCiEntity> songcies;
    public SongCi(){
        int num = new Random().nextInt(23) * 1000;
        String filename = String.format("ci.song.%d.json", num);
        InputStream is = SongCi.class.getClassLoader().getResourceAsStream("poetry/ci/" + filename);
        songcies = JSONFactory.stringToObject(JSONFactory.read(is).toString(), List.class, SongCiEntity.class);
    }
    @Override
    public String content() {
        SongCiEntity entity = rangeSongCi();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n《").append(entity.getRhythmic()).append("》")
                .append("  --").append(entity.getAuthor()).append("\r\n\r\n");
        for(String line:entity.getParagraphs()){
            sb.append(line).append("\r\n");
        }
        return sb.toString();
    }

    private SongCiEntity rangeSongCi(){
        return songcies.get(new Random().nextInt(songcies.size()));
    }
}
