package com.hyunbenny.snsApplication.repository;

import com.hyunbenny.snsApplication.model.entity.LikeEntity;
import com.hyunbenny.snsApplication.model.entity.PostEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByPostAndUser(PostEntity post, UserEntity user);

    @Query(value = "SELECT COUNT(*) FROM LikeEntity e WHERE e.post = :post")
    Long countByPost(@Param("post") PostEntity post);

}
