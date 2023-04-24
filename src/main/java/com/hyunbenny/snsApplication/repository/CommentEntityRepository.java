package com.hyunbenny.snsApplication.repository;

import com.hyunbenny.snsApplication.model.entity.CommentEntity;
import com.hyunbenny.snsApplication.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
}
