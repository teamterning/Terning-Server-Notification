package org.terning.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terning.user.application.fcmtoken.update.FcmTokenUpdateService;
import org.terning.user.application.fcmtoken.validate.FcmTokenValidateService;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepository;
import org.terning.user.domain.vo.AccountStatus;
import org.terning.user.domain.vo.FcmToken;
import org.terning.user.domain.vo.PushNotificationStatus;
import org.terning.user.domain.vo.UserName;
import org.terning.user.dto.request.CreateUserRequest;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.request.UpdateFcmTokenRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final FcmTokenValidateService fcmTokenValidateService;
    private final FcmTokenUpdateService fcmTokenUpdateService;
    public final UserRepository userRepository;

    public FcmTokenReissueRequiredResponse isFcmTokenReissueRequired(FcmTokenReissueRequiredRequest request) {
        return fcmTokenValidateService.isFcmTokenReissueRequired(request);
    }

    @Transactional
    public void updateFcmToken(Long oUserId, String newToken) {
        User user = userRepository.findByOUserId(oUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.updateFcmToken(newToken);
        userRepository.save(user);
    }

    @Transactional
    public void createUser(CreateUserRequest request) {
        PushNotificationStatus pushStatus = PushNotificationStatus.from(request.pushStatus());
        AccountStatus accountStatus = AccountStatus.from(request.accountStatus());
        User user = User.of(
                request.oUserId(),
                UserName.from(request.name()),
                FcmToken.from(request.fcmToken()),
                pushStatus,
                request.authType(),
                accountStatus
        );
        userRepository.save(user);
    }

    @Transactional
    public void updatePushStatus(Long oUserId, String newPushStatus) {
        User user = userRepository.findByOUserId(oUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.setPushStatus(PushNotificationStatus.from(newPushStatus));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserName(Long oUserId, String newName) {
        User user = userRepository.findByOUserId(oUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.setName(UserName.from(newName));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long oUserId) {
        User user = userRepository.findByOUserId(oUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
