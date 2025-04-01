package org.terning.notification.dto.request;

public record NotificationCreateRequest(String template) {
    public static NotificationCreateRequest of(String template) {
        return new NotificationCreateRequest(template);
    }
}
