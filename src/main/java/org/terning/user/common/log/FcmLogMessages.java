package org.terning.user.common.log;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class FcmLogMessages {

    public static final String INIT_SUCCESS = "✅Firebase 초기화 성공";
    public static final String INIT_FAILED = "❌Firebase 초기화 실패";
    public static final String PUSH_SENT_SUCCESS = "📤푸시 알림 전송 성공";
    public static final String PUSH_SENT_FAILURE = "❗푸시 알림 전송 실패";
}
