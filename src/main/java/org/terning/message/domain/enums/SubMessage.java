package org.terning.message.domain.enums;

import lombok.RequiredArgsConstructor;
import org.terning.message.domain.AbstractMessageTemplate;
import org.terning.message.domain.MessageTemplate;

import java.util.Map;

@RequiredArgsConstructor
public enum SubMessage implements MessageTemplate {

    INTERESTED_ANNOUNCEMENT_DETAIL("곧 마감하는 공고 일정, 내 캘린더에서 확인해 보세요", false),
    RECENTLY_POSTED_INTERNSHIP_DETAIL("{username}님의 계획에 맞춰, 딱 맞는 인턴 공고를 골라볼 수 있어요!", true),
    TRENDING_INTERNSHIP_DETAIL("{username}을 위해 준비한 이번주 인기 공고, 놓치기 전에 살펴보세요!", true),

    ;

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



