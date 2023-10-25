package ru.netology.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.netology.dto.PostDTO;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;
//import net.javaguides.springboot.repository.UserRepository;

import java.util.List;
import java.util.Optional;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class NotFoundException extends RuntimeException {
    }

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
//    private PostDTO convertEntityToDto(Post post) {
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE);
//        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
//        return postDTO;
//    }
//
//    private Post convertDtoToEntity(PostDTO dto) {
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE);
//        Post post = modelMapper.map(dto, Post.class);
//        return post;
//    }


    private Post convertDtoToEntity(PostDTO dto) {
        // преобразование полей DTO в соответствующие поля сущности
        var entity = new Post();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setRemoved(dto.isRemoved());
        return entity;
    }

    private PostDTO convertEntityToDto(Post entity) {
        // преобразование полей сущности в соответствующие поля DTO
        var dto = new PostDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setRemoved(entity.isRemoved());
        return dto;
    }
    //-----------------------------------------------------------

    public PostDTO getById(long id) {
        boolean flagRemover = repository.getById(id).stream()
                .map(Post::isRemoved)
                .anyMatch(Boolean::booleanValue);

        if (!flagRemover) {
            return convertEntityToDto(repository.getById(id).orElseThrow(NotFoundException::new));
        } else {
            throw new NotFoundException();
        }
    }

    // entity - сущность которая храниться в базе
    public PostDTO save(PostDTO dto) {
        var entity = convertDtoToEntity(dto);
        //  System.out.println("entity: " + entity);
        var savedEntity = repository.save(entity);
        return convertEntityToDto(savedEntity);
    }


//    Optional<PostDTO> modifiedDTO = dtoList.stream()
//            .peek(dto -> {
//                dto.setTitle("Modified Title");
//                dto.setContent("Modified Content");
//            })
//            .findFirst();


    public void removeById(@PathVariable Long id) {
        // меняем
        Optional<Post> firstObject = repository.getById(id).stream()
                .peek(o -> o.setRemoved(true)).findFirst();
        repository.save(firstObject.get());
    }
}
