package org.terning.message.domain.enums;

import lombok.RequiredArgsConstructor;
import org.terning.message.domain.AbstractMessageTemplate;
import org.terning.message.domain.MessageTemplate;

import java.util.Map;

@RequiredArgsConstructor
public enum MainMessage implements MessageTemplate {

    INTERESTED_ANNOUNCEMENT("🚨앗, 공고 마감 직전!", false),
    RECENTLY_POSTED_INTERNSHIP("🔫갓 나온 공고, 지금 확인!", false),
    TRENDING_INTERNSHIP("😱나 빼고 다 보고 간 인기공고…..", false);

    private final String template;
    private final boolean requiresFormatting;

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
        return new AbstractMessageTemplate(template, requiresFormatting){}.format(params);
    }
}

