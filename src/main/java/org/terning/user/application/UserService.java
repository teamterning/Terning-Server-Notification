package org.terning.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terning.user.application.fcmtoken.validate.FcmTokenValidateService;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepository;
import org.terning.user.domain.vo.AccountStatus;
import org.terning.user.domain.vo.FcmToken;
import org.terning.user.domain.vo.PushNotificationStatus;
import org.terning.user.domain.vo.UserName;
import org.terning.user.dto.request.CreateUserRequest;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final FcmTokenValidateService fcmTokenValidateService;
    public final UserRepository userRepository;

    public FcmTokenReissueRequiredResponse isFcmTokenReissueRequired(FcmTokenReissueRequiredRequest request) {
        return fcmTokenValidateService.isFcmTokenReissueRequired(request);
    }

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
}