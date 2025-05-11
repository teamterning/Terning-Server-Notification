package org.terning.user.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.terning.fcm.validate.FcmTokenValidator;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

@Embeddable
@EqualsAndHashCode
public class FcmToken {

    private final String value;

    protected FcmToken() {
        this.value = null;
    }

    private FcmToken(String value) {
        validateToken(value);
        this.value = value;
    }

    public static FcmToken from(String value) {
        return new FcmToken(value);
    }

    public boolean isExpiredWith(FcmTokenValidator validator) {
        return validator.isExpiredWith(this);
    }

    public FcmToken updateValue(String newValue) {
        return FcmToken.from(newValue);
    }

    public String value() {
        return value;
    }

    private void validateToken(String value) {
        validateNotNull(value);
    }

    private void validateNotNull(String value) {
        if (value == null) {
            throw new UserException(UserErrorCode.FCM_TOKEN_NOT_NULL);
        }
    }
}

