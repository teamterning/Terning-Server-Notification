package org.terning.scrap.common.failure;

import org.terning.global.failure.BaseException;

public class ScrapException extends BaseException {
    public ScrapException(ScrapErrorCode errorCode) {
        super(errorCode);
    }
}
