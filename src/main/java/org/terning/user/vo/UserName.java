package org.terning.user.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

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

    // TODO: UserException 클래스를 만들어서 BaseException을 상속받아서 사용하도록 수정
    // TODO: BaseException 구현 및 global 예외처리 구현
    private void validateName(String name) {
        validateNull(name);
        validateLength(name);
        validateInvalidCharacters(name);
    }

    private void validateNull(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null일 수 없습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("이름의 길이는 12자를 초과할 수 없습니다.");
        }
    }

    private void validateInvalidCharacters(String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("이름의 구성은 문자(한글, 영어), 숫자만 가능합니다.");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
