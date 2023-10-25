package ru.netology.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.netology.dto.PostDTO;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;
//import net.javaguides.springboot.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


//@Service
//public class PostService {
//
//    // сервис завязан на интерфейс, а не на конкретную реализацию
//    private final PostRepository repository;
//
//    public PostService(PostRepository repository) {
//        this.repository = repository;
//    }
//
//    public List<Post> all() {
//        return repository.all();
//    }
//
//    public Post getById(long id) {
//        return repository.getById(id).orElseThrow(NotFoundException::new);
//    }
//
//    public Post save(Post post) {
//        return repository.save(post);
//    }
//
//    public void removeById(@PathVariable Long id) {
//        repository.removeById(id);
//    }
//}

@Service
public class PostService {

    private ModelMapper modelMapper;
    private static final String REMOVED = "removed";

    // сервис завязан на интерфейс, а не на конкретную реализацию
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<PostDTO> all() {
        return repository.all().
                stream()
                .map(this::convertEntityToDto)
                .filter(obj -> !obj.isRemoved())
                .collect(Collectors.toList());
    }

    //------------------------Конвертеры-------------------------
    // конвертер, mapper
    private PostDTO convertEntityToDto(Post post) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        return postDTO;
    }

    private Post convertDtoToEntity(PostDTO dto) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        Post post = modelMapper.map(dto, Post.class);
        return post;
    }


//    private Post convertDtoToEntity(PostDTO dto) {
//        // преобразование полей DTO в соответствующие поля сущности
//        var entity = new Post();
//        entity.setId(dto.getId());
//        entity.setContent(dto.getContent());
//        entity.setRemoved(dto.isRemoved());
//        return entity;
//    }
//
//    private PostDTO convertEntityToDto(Post entity) {
//        // преобразование полей сущности в соответствующие поля DTO
//        var dto = new PostDTO();
//        dto.setId(entity.getId());
//        dto.setContent(entity.getContent());
//        dto.setRemoved(entity.isRemoved());
//        // другие поля...
//
//        return dto;
//    }


    //-----------------------------------------------------------

    public PostDTO getById(long id) {
        return convertEntityToDto(repository.getById(id).orElseThrow(NotFoundException::new));
    }

    // entity - сущность которая храниться в базе
    public PostDTO save(PostDTO dto) {
        var entity = convertDtoToEntity(dto);
      //  System.out.println("entity: " + entity);
        var savedEntity = repository.save(entity);
        return convertEntityToDto(savedEntity);
    }

    public void removeById(@PathVariable Long id) {
        repository.removeById(id);
    }
}
