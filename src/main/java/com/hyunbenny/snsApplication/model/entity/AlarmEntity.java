package com.hyunbenny.snsApplication.model.entity;

import com.hyunbenny.snsApplication.model.alarm.AlarmArgs;
import com.hyunbenny.snsApplication.model.alarm.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "\"alarm\"", indexes = {
        @Index(name = "alarm_entity_user_id_idx", columnList = "user_id")
})
@Getter
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) // jsonb타입을 사용하기 위해서는 추가해줘야 @TypeDef을 한다.
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; // 알람을 받을 사람

    @Column(name = "alarm_type")
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private AlarmArgs alarmArgs;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    public void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    protected AlarmEntity() {}

    private AlarmEntity(UserEntity user, AlarmType alarmType, AlarmArgs alarmArgs) {
        this.user = user;
        this.alarmType = alarmType;
        this.alarmArgs = alarmArgs;
        createdAt();
    }

    private AlarmEntity(Long id, UserEntity user, AlarmType alarmType, AlarmArgs alarmArgs) {
        this.id = id;
        this.user = user;
        this.alarmType = alarmType;
        this.alarmArgs = alarmArgs;
        createdAt();
    }

    public static AlarmEntity of(UserEntity user, AlarmType alarmType, AlarmArgs alarmArgs) {
        return new AlarmEntity(user, alarmType, alarmArgs);
    }

    public static AlarmEntity of(Long id, UserEntity user, AlarmType alarmType, AlarmArgs alarmArgs) {
        return new AlarmEntity(id, user, alarmType, alarmArgs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlarmEntity that = (AlarmEntity) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
