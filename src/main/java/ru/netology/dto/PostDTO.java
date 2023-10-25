package ru.netology.dto;

import lombok.Data;

// @Data – это сокращенная аннотация,сочетающая возможности@ToString,@EqualsAndHashCode,
// @Getter @Setter и@RequiredArgsConstructor.Так что@Data генерирует весь шаблонный код,вовлеченный в работу с
// объектами POJO(Plain Old Java Objects).Это,в частности,дает нам геттеры для всех полей,сеттеры для всех
// нефинальных полей,правильные реализации toString,equals и hashCode,охватывающие все поля класса,
// а также конструктор для всех финальных полей. С Lombok

@Data
public class PostDTO {
    private long id;
    private String content;
    private boolean removed;
}
