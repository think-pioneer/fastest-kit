package xyz.thinktest.fastestapi.core.internal.tool.poetry.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author: aruba
 * @date: 2022-01-31
 */
public class ShijingEntity implements Serializable {
    private static final long serialVersionUID = 1626667330718847813L;
    private String title;
    private String chapter;
    private String section;
    private List<String> content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ShijingEntity{" +
                "title='" + title + '\'' +
                ", chapter='" + chapter + '\'' +
                ", section='" + section + '\'' +
                ", content=" + content +
                '}';
    }
}
