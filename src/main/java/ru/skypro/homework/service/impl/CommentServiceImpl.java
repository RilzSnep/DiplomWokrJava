package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public Comments getComments(Integer adId) {
        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        List<CommentEntity> commentEntities = commentRepository.findByAd(adEntity);
        List<Comment> commentDtos = commentEntities.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        Comments comments = new Comments();
        comments.setCount(commentDtos.size());
        comments.setResults(commentDtos);
        return comments;
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment comment, Authentication authentication) {
        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        UserEntity author = getUserFromAuthentication(authentication);

        CommentEntity commentEntity = commentMapper.toEntity(comment, author);
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(Instant.now());

        CommentEntity savedComment = commentRepository.save(commentEntity);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new RuntimeException("Comment does not belong to this ad");
        }

        if (!isCommentOwnerOrAdmin(commentId, authentication)) {
            throw new RuntimeException("Access denied");
        }

        commentRepository.delete(commentEntity);
    }

    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment, Authentication authentication) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new RuntimeException("Comment does not belong to this ad");
        }

        if (!isCommentOwnerOrAdmin(commentId, authentication)) {
            throw new RuntimeException("Access denied");
        }

        commentMapper.updateEntityFromDto(comment, commentEntity);
        CommentEntity updatedComment = commentRepository.save(commentEntity);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    public boolean isCommentOwnerOrAdmin(Integer commentId, Authentication authentication) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String currentUsername = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return commentEntity.getAuthor().getId().equals(currentUser.getId()) ||
                currentUser.getRole() == Role.ADMIN;
    }

    private UserEntity getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}