package xyz.thinktest.fastestapi.core.internal.tool.poetry.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author: aruba
 * @date: 2022-02-01
 */
public class TangShiEntity implements Serializable {
    private static final long serialVersionUID = -625287396384763073L;
    private String id;
    private String author;
    private String title;
    private List<String> tags;
    private List<String> paragraphs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "TangShiEntity{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", tags=" + tags +
                ", paragraphs=" + paragraphs +
                '}';
    }
}
