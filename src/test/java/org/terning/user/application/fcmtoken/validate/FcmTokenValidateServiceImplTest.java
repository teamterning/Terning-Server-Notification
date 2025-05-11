package org.terning.user.application.fcmtoken.validate;

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
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FCM 토큰 만료 검증 테스트")
class FcmTokenValidateServiceImplTest {

    private UserRepository userRepository;
    private FakeFcmTokenValidator fakeValidator;
    private FcmTokenValidateService service;

    private Long activeUserId;
    private Long inactiveUserId;
    private Long pushDisabledUserId;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryTest();
        fakeValidator = new FakeFcmTokenValidator();
        service = new FcmTokenValidateServiceImpl(userRepository, fakeValidator);

        activeUserId = saveUser("장순", "test-token", PushNotificationStatus.ENABLED, AuthType.KAKAO, AccountStatus.ACTIVE);
        inactiveUserId = saveUser("탈퇴유저", "withdrawn-token", PushNotificationStatus.ENABLED, AuthType.KAKAO, AccountStatus.WITHDRAWN);
        pushDisabledUserId = saveUser("푸시꺼진유저", "some-token", PushNotificationStatus.DISABLED, AuthType.KAKAO, AccountStatus.ACTIVE);
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("FCM 토큰이 만료된 경우 true를 반환한다")
        void returnsTrue_whenTokenIsExpired() {
            fakeValidator.setExpired(true);

            FcmTokenReissueRequiredRequest request = FcmTokenReissueRequiredRequest.of(activeUserId);
            FcmTokenReissueRequiredResponse response = service.isFcmTokenReissueRequired(request);

            assertThat(response.reissueRequired()).isTrue();
        }

        @Test
        @DisplayName("FCM 토큰이 만료되지 않은 경우 false를 반환한다")
        void returnsFalse_whenTokenIsNotExpired() {
            fakeValidator.setExpired(false);

            FcmTokenReissueRequiredRequest request = FcmTokenReissueRequiredRequest.of(activeUserId);
            FcmTokenReissueRequiredResponse response = service.isFcmTokenReissueRequired(request);

            assertThat(response.reissueRequired()).isFalse();
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("존재하지 않는 사용자 ID이면 예외를 던진다")
        void throwsException_whenUserNotFound() {
            Long notExistId = activeUserId + 1000;

            FcmTokenReissueRequiredRequest request = FcmTokenReissueRequiredRequest.of(notExistId);

            assertThatThrownBy(() -> service.isFcmTokenReissueRequired(request))
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("계정 상태가 탈퇴인 경우 false를 반환한다")
        void returnsFalse_whenAccountIsWithdrawn() {
            fakeValidator.setExpired(true);

            FcmTokenReissueRequiredRequest request = FcmTokenReissueRequiredRequest.of(inactiveUserId);
            FcmTokenReissueRequiredResponse response = service.isFcmTokenReissueRequired(request);

            assertThat(response.reissueRequired()).isFalse();
        }

        @Test
        @DisplayName("푸시 알림이 비활성화된 경우 false를 반환한다")
        void returnsFalse_whenPushIsDisabled() {
            fakeValidator.setExpired(true);

            FcmTokenReissueRequiredRequest request = FcmTokenReissueRequiredRequest.of(pushDisabledUserId);
            FcmTokenReissueRequiredResponse response = service.isFcmTokenReissueRequired(request);

            assertThat(response.reissueRequired()).isFalse();
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
