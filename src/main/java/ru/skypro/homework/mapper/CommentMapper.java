package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class CommentMapper {

    public Comment toDto(CommentEntity entity) {
        Comment dto = new Comment();
        dto.setPk(entity.getId());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorImage(entity.getAuthor().getImage());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setCreatedAt(entity.getCreatedAt().toEpochMilli());
        dto.setText(entity.getText());
        return dto;
    }

    public CommentEntity toEntity(CreateOrUpdateComment dto, UserEntity author) {
        CommentEntity entity = new CommentEntity();
        entity.setText(dto.getText());
        entity.setAuthor(author);
        entity.setCreatedAt(java.time.Instant.now());
        return entity;
    }

    public void updateEntityFromDto(CreateOrUpdateComment dto, CommentEntity entity) {
        if (dto.getText() != null) {
            entity.setText(dto.getText());
        }
    }
}