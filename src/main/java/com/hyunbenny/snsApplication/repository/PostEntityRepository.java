package com.hyunbenny.snsApplication.repository;

import com.hyunbenny.snsApplication.model.entity.PostEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAllByUser(UserEntity user, Pageable pageable);

}
