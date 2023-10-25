package ru.netology.model;

public class PostDTO {
    private long id;

    private String content;

    private boolean removed;

    public PostDTO() {
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.removed = post.isRemoved();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
