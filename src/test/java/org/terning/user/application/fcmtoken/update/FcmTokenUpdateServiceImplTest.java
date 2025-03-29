package org.terning.user.application.fcmtoken.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepository;
import org.terning.user.domain.UserRepositoryTest;
import org.terning.user.domain.vo.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FCM 토큰 업데이트 서비스 테스트")
class FcmTokenUpdateServiceImplTest {

    private UserRepository userRepository;
    private FcmTokenUpdateService service;

    private Long userId;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryTest();
        service = new FcmTokenUpdateServiceImpl(userRepository);

        userId = saveUser("장순", "old-token", PushNotificationStatus.ENABLED, AuthType.KAKAO, AccountStatus.ACTIVE);
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("FCM 토큰을 새로운 값으로 업데이트한다")
        void updateFcmToken_successfully() {
            // given
            String newToken = "new-token-value";

            // when
            service.updateFcmToken(userId, newToken);

            // then
            User updatedUser = userRepository.findById(userId).orElseThrow();
            assertThat(updatedUser.getToken().value()).isEqualTo(newToken);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("존재하지 않는 사용자 ID로 요청하면 예외를 던진다")
        void throwsException_whenUserNotFound() {
            // given
            Long invalidUserId = userId + 1000;
            String newToken = "new-token-value";

            // when & then
            assertThatThrownBy(() -> service.updateFcmToken(invalidUserId, newToken))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("새로운 토큰 값이 null이면 예외를 던진다")
        void throwsException_whenTokenIsNull() {
            // given
            String newToken = null;

            // when & then
            assertThatThrownBy(() -> service.updateFcmToken(userId, newToken))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.FCM_TOKEN_NOT_NULL.getMessage());
        }
    }

    private Long saveUser(String name, String token, PushNotificationStatus pushStatus, AuthType authType, AccountStatus accountStatus) {
        User user = User.of(
                UserName.from(name),
                FcmToken.from(token),
                pushStatus,
                authType,
                accountStatus
        );
        return userRepository.save(user).getId();
    }
}