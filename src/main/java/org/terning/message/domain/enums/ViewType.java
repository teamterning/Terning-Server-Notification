package org.terning.message.domain.enums;

public enum ViewType {
    CALENDAR,
    HOME,
    SEARCH;

    public static ViewType fromTemplate(MessageTemplateType tmpl) {
        return switch (tmpl) {
            case INTERESTED_ANNOUNCEMENT_REMINDER -> CALENDAR;
            case RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION -> HOME;
            case TRENDING_INTERNSHIP_ALERT -> SEARCH;
        };
    }
}
