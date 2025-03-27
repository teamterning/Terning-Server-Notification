package org.terning.message.domain;

import org.terning.message.common.failure.MessageErrorCode;
import org.terning.message.common.failure.MessageException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public abstract class AbstractMessageTemplate implements MessageTemplate {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\w+)}");

    private final String template;
    private final boolean requiresFormatting;

    protected AbstractMessageTemplate(String template, boolean requiresFormatting) {
        this.template = template;
        this.requiresFormatting = requiresFormatting;
    }

    @Override
    public String value() {
        return template;
    }

    @Override
    public boolean needsFormatting() {
        return requiresFormatting;
    }

    @Override
    public String format(Map<String, String> params) {
        if (!requiresFormatting) {
            return template;
        }

        validateParams(params);
        return replacePlaceholders(params);
    }

    private void validateParams(Map<String, String> params) {
        if (isNull(params) || params.isEmpty()) {
            throw new MessageException(MessageErrorCode.MISSING_FORMATTING_PARAMS);
        }
    }

    private String replacePlaceholders(Map<String, String> params) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            String replacement = params.get(key);

            if (replacement == null) {
                throw new MessageException(MessageErrorCode.MISSING_PLACEHOLDER_VALUE);
            }

            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
