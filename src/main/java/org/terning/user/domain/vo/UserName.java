package org.terning.user.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.terning.user.common.failure.UserErrorCode;
import org.terning.user.common.failure.UserException;

@Embeddable
@EqualsAndHashCode
public class UserName {
    private static final int NAME_MAX_LENGTH = 12;
    private static final String NAME_REGEX = "^[a-zA-Z0-9가-힣 ]*$";

    private final String value;

    protected UserName() {
        this.value = null;
    }

    private UserName(String value) {
        validateName(value);
        this.value = value;
    }

    public static UserName from(String value) {
        return new UserName(value);
    }

    public String value() {
        return value;
    }

    private void validateName(String value) {
        validateNull(value);
        validateLength(value);
        validateInvalidCharacters(value);
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new UserException(UserErrorCode.USER_NAME_NOT_NULL);
        }
    }

    private void validateLength(String value) {
        if (value.length() > NAME_MAX_LENGTH) {
            throw new UserException(UserErrorCode.USER_NAME_LENGTH_EXCEEDED);
        }
    }

    private void validateInvalidCharacters(String value) {
        if (!value.matches(NAME_REGEX)) {
            throw new UserException(UserErrorCode.INVALID_USER_NAME);
        }
    }
}
