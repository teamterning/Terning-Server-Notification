package org.terning.user.application.fcmtoken.validate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terning.fcm.validate.FcmTokenValidator;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepository;
import org.terning.user.dto.request.FcmTokenReissueRequiredRequest;
import org.terning.user.dto.response.FcmTokenReissueRequiredResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmTokenValidateServiceImpl implements FcmTokenValidateService {

    private final UserRepository userRepository;
    private final FcmTokenValidator fcmTokenValidator;

    @Override
    public FcmTokenReissueRequiredResponse isFcmTokenReissueRequired(FcmTokenReissueRequiredRequest request) {
        User user = findUserById(request.userId());
        boolean expired = user.isFcmTokenExpired(fcmTokenValidator);
        return FcmTokenReissueRequiredResponse.of(expired);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}