package xyz.think.fastest.core.internal.tool.poetry.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author: aruba
 * @date: 2022-02-01
 */
public class SongCiEntity implements Serializable {
    private static final long serialVersionUID = 2434276775713056335L;
    private String rhythmic;
    private String author;
    private List<String> tags;
    private List<String> paragraphs;

    public String getRhythmic() {
        return rhythmic;
    }

    public void setRhythmic(String rhythmic) {
        this.rhythmic = rhythmic;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SongCiEntity{" +
                "rhythmic='" + rhythmic + '\'' +
                ", author='" + author + '\'' +
                ", tags=" + tags +
                ", paragraphs=" + paragraphs +
                '}';
    }
}
