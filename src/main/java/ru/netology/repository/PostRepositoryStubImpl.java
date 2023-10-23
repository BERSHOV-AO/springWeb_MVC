package ru.netology.repository;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {

    private final AtomicLong postId;
    private final Map<Long, Post> postsMap;
    private static final String REMOVED = "removed";

    public PostRepositoryStubImpl() {
        postId = new AtomicLong(0);
        postsMap = new ConcurrentHashMap<>();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class NotFoundException extends RuntimeException {
    }

    public List<Post> all() {
        List<Post> survivorPostsLst = new ArrayList<>();
        for (Post post : postsMap.values()) {
            if (!post.getContent().equals(REMOVED)) {
                survivorPostsLst.add(post);
            }
        }
        return survivorPostsLst;
    }

    public Optional<Post> getById(long id) {
        if (!postsMap.get(id).getContent().equals(REMOVED)) {
            return Optional.ofNullable(postsMap.get(id));
        } else {
            throw new NotFoundException();
        }
    }

    public Post save(Post post) {
        long specificID = post.getId();
        // если больше 0 и наличие
        if (specificID > 0 && postsMap.containsKey(specificID)) {
            if (!postsMap.get(specificID).getContent().equals(REMOVED)) {
                // заменяем
                postsMap.replace(specificID, post);
            } else {
                throw new NotFoundException();
            }
        } else {
            long newIdPost = specificID == 0 ? postId.incrementAndGet() : specificID;
            post.setId(newIdPost);
            // добавление нового элемента в postsMap
            postsMap.put(newIdPost, post);
        }
        return post;
    }

    public void removeById(long id) {
        if (!postsMap.get(id).getContent().equals(REMOVED)) {
            postsMap.get(id).setContent(REMOVED);
        } else {
            throw new NotFoundException();
        }
    }
}
