package org.terning.message.domain;

import java.util.Map;

public interface MessageTemplate {
    String value();
    boolean needsFormatting();
    String format(Map<String, String> params);
}

