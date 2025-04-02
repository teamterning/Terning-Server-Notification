package org.terning.user.dto.request;

import org.terning.user.domain.vo.AuthType;

public record CreateUserRequest(
        Long oUserId,
        String name,
        AuthType authType,
        String fcmToken,
        String pushStatus,
        String accountStatus
) {
}
