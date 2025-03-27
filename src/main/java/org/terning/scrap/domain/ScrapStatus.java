package org.terning.scrap.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.terning.scrap.common.failure.ScrapErrorCode;
import org.terning.scrap.common.failure.ScrapException;

import java.util.Arrays;

public enum ScrapStatus {
    SCRAPPED("scrapped"),
    UNSCRAPPED("unscrapped");

    private final String value;

    ScrapStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ScrapStatus from(String input) {
        return Arrays.stream(values())
                .filter(status -> status.value.equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new ScrapException(ScrapErrorCode.INVALID_SCRAP_STATUS));
    }

    public static boolean isValid(String input) {
        return Arrays.stream(values())
                .anyMatch(status -> status.value.equalsIgnoreCase(input));
    }

    public boolean isScrapped() {
        return this == SCRAPPED;
    }

    public boolean isUnscrapped() {
        return this == UNSCRAPPED;
    }

    public ScrapStatus scrap() {
        if (this == SCRAPPED) {
            throw new ScrapException(ScrapErrorCode.ALREADY_SCRAPPED);
        }
        return SCRAPPED;
    }

    public ScrapStatus unscrap() {
        if (this == UNSCRAPPED) {
            throw new ScrapException(ScrapErrorCode.ALREADY_UNSCRAPPED);
        }
        return UNSCRAPPED;
    }

    @JsonValue
    public String value() {
        return value;
    }
}

