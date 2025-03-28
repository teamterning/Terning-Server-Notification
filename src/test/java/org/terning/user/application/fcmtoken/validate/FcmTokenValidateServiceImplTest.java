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
    private Long userId;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryTest();
        fakeValidator = new FakeFcmTokenValidator();
        service = new FcmTokenValidateServiceImpl(userRepository, fakeValidator);

        User user = User.of(
                UserName.from("장순"),
                FcmToken.from("test-token"),
                PushNotificationStatus.from("ENABLED"),
                AuthType.from("KAKAO"),
                AccountStatus.from("ACTIVE")
        );
        userId = userRepository.save(user).getId();
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("FCM 토큰이 만료된 경우 true를 반환한다")
        void returnsTrue_whenTokenIsExpired() {
            // given
            fakeValidator.setExpired(true);

            // when
            FcmTokenReissueRequiredRequest request = new FcmTokenReissueRequiredRequest(userId);
            FcmTokenReissueRequiredResponse response = service.isFcmTokenReissueRequired(request);

            // then
            assertThat(response.reissueRequired()).isTrue();
        }

        @Test
        @DisplayName("FCM 토큰이 만료되지 않은 경우 false를 반환한다")
        void returnsFalse_whenTokenIsNotExpired() {
            // given
            fakeValidator.setExpired(false);

            // when
            FcmTokenReissueRequiredRequest request = new FcmTokenReissueRequiredRequest(userId);
            FcmTokenReissueRequiredResponse response = service.isFcmTokenReissueRequired(request);

            // then
            assertThat(response.reissueRequired()).isFalse();
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("존재하지 않는 사용자 ID이면 예외를 던진다")
        void throwsException_whenUserNotFound() {
            // given
            fakeValidator.setExpired(false);
            Long notExistId = userId + 1000;

            // expect
            assertThatThrownBy(() -> {
                FcmTokenReissueRequiredRequest request = new FcmTokenReissueRequiredRequest(notExistId);
                service.isFcmTokenReissueRequired(request);
            }).isInstanceOf(UserException.class)
                    .hasMessageContaining(UserErrorCode.USER_NOT_FOUND.getMessage());
        }
    }
}


