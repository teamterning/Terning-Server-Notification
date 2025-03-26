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

    private final String name;

    protected UserName() {
        this.name = null;
    }

    private UserName(String name) {
        validateName(name);
        this.name = name;
    }

    public static UserName from(String name) {
        return new UserName(name);
    }

    public String value() {
        return name;
    }

    private void validateName(String name) {
        validateNull(name);
        validateLength(name);
        validateInvalidCharacters(name);
    }

    private void validateNull(String name) {
        if (name == null) {
            throw new UserException(UserErrorCode.USER_NAME_NOT_NULL);
        }
    }

    private void validateLength(String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            throw new UserException(UserErrorCode.USER_NAME_LENGTH_EXCEEDED);
        }
    }

    private void validateInvalidCharacters(String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new UserException(UserErrorCode.INVALID_USER_NAME);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
