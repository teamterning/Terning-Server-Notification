package org.terning.message.domain.enums;

import lombok.RequiredArgsConstructor;
import org.terning.message.domain.AbstractMessageTemplate;
import org.terning.message.domain.MessageTemplate;

import java.util.Map;

@RequiredArgsConstructor
public enum SubMessage implements MessageTemplate {

    INTERESTED_ANNOUNCEMENT_DETAIL("스크랩한 인턴 공고의 마감 일정, {username}님의 캘린더에서 한눈에 확인해 볼까요!", true),
    RECENTLY_POSTED_INTERNSHIP_DETAIL("{username}님의 원하는 직무와 계획에 맞춰, 딱 맞는 새 인턴 공고를 골라볼 수 있어요!", true),
    TRENDING_INTERNSHIP_DETAIL("{username}님을 위해 터닝이가 인기 많은 인턴 공고를 가져왔어요! 놓치기 전에 살펴보세요.", true),

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



