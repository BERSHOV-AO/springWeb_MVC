package ru.netology.repository;

import org.springframework.stereotype.Repository;
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

    public PostRepositoryStubImpl() {
        postId = new AtomicLong(0);
        postsMap = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(postsMap.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postsMap.get(id));
    }

    public Post save(Post post) {
        long specificID = post.getId();
        // если больше 0 и наличие
        if (specificID > 0 && postsMap.containsKey(specificID)) {
            // заменяем
            postsMap.replace(specificID, post);
        } else {
            long newIdPost = specificID == 0 ? postId.incrementAndGet() : specificID;
            post.setId(newIdPost);
            // добавление нового элемента в postsMap
            postsMap.put(newIdPost, post);
        }
        return post;
    }

    public void removeById(long id) {
        postsMap.remove(id);
    }
}
