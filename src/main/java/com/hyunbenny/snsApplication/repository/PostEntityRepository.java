package com.hyunbenny.snsApplication.repository;

import com.hyunbenny.snsApplication.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
}
