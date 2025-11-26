package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.AdEntity;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByAd(AdEntity ad);
    List<CommentEntity> findByAdId(Integer adId);
    void deleteByAdId(Integer adId);
}