package org.terning.message.domain.enums;

import lombok.RequiredArgsConstructor;
import org.terning.message.domain.AbstractMessageTemplate;
import org.terning.message.domain.MessageTemplate;

import java.util.Map;

@RequiredArgsConstructor
public enum MainMessage implements MessageTemplate {

    INTERESTED_ANNOUNCEMENT("📢 터닝이가 관심 공고 일정을 알려드릴게요!", false),
    RECENTLY_POSTED_INTERNSHIP("🎯 터닝이가 새롭게 올라온 인턴 공고를 골라왔어요!", false),
    TRENDING_INTERNSHIP("👀 다들 이 인턴 공고를 보고 있어요!", false);

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

