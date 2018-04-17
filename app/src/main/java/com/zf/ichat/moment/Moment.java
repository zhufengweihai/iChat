package com.zf.ichat.moment;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Moment {
    private String name = null;
    private String avatar = null;
    private String content = null;
    private List<String> photos = new ArrayList<>();
    private List<String> likes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public void addPhoto(String photo) {
        photos.add(photo);
    }

    public void addLike(String name) {
        likes.add(name);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
