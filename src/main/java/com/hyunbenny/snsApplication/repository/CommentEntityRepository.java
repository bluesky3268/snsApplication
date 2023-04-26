package com.hyunbenny.snsApplication.repository;

import com.hyunbenny.snsApplication.model.entity.CommentEntity;
import com.hyunbenny.snsApplication.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPostId(PostEntity postEntity, Pageable pageable);

//    void deleteAllByPost(PostEntity postEntity); // 그냥 delete 쿼리만 날리는 게 아니라 영속성 관리를 위해서 데이터를 다 가지고 와서 삭제를 한다.(굳이 가지고 올 필요가 없는데 비효율적이지 않니..?)

    // 그래서 JPA에서 제공하는 delete가 아니라 직접 쿼리를 짜는 게 낫다.
    @Transactional
    @Modifying
    @Query("UPDATE CommentEntity entity SET deleted_at = NOW() WHERE entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);

}
