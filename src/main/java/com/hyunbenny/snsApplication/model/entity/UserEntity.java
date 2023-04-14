package com.hyunbenny.snsApplication.model.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
public class UserEntity {

    @Id
    private Long id;

    private String username;
    private String password;

    @Builder
    public UserEntity(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
