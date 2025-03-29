package org.terning.user.application.fcmtoken.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class FcmTokenUpdateServiceImpl implements FcmTokenUpdateService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateFcmToken(Long userId, String newToken) {
        User user = findUserById(userId);
        user.updateFcmToken(newToken);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
