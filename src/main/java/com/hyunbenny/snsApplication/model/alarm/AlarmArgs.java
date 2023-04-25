package com.hyunbenny.snsApplication.model.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmArgs {

    private Long fromUserId; // 알람을 발생시킨 사람(ex_ 댓글은 작성한 사람, 좋아요를 누른 사람의 id)
    private Long targetId; // 알람의 주체(ex_ 알람이 발생된 포스트의 id, 좋아요가 눌러진 포스트의 id)
}
