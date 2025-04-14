package org.terning.notification.application.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terning.message.domain.Message;
import org.terning.message.domain.enums.MessageTargetType;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.common.failure.NotificationErrorCode;
import org.terning.notification.common.failure.NotificationException;
import org.terning.notification.domain.Notification;
import org.terning.notification.domain.NotificationsRepository;
import org.terning.notification.domain.vo.ScheduledTime;
import org.terning.scrap.domain.ScrapRepository;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationWriterImpl implements NotificationWriter {

    private static final String USERNAME = "username";

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final NotificationsRepository notificationsRepository;

    @Override
    @Transactional
    public void write(MessageTemplateType template, ScheduledTime sendTime) {
        List<User> targetUsers = fetchTargetUsers(template.targetType());
        List<User> filteredUsers = targetUsers.stream()
                .filter(user -> user.getToken() != null
                        && user.getToken().value() != null
                        && !user.getToken().value().trim().isEmpty())
                .toList();
        List<Notification> notifications = createNotifications(filteredUsers, template, sendTime);
        saveNotifications(notifications);
    }

    private List<User> fetchTargetUsers(MessageTargetType targetType) {
        List<User> users = switch (targetType) {
            case SCRAPPED_USER -> scrapRepository.findDistinctScrappedUsers();
            case ALL_USERS -> userRepository.findAll();
            default -> throw new NotificationException(NotificationErrorCode.INVALID_TARGET_TYPE);
        };

        return users.stream()
                .filter(User::canReceivePushNotification)
                .toList();
    }

    private List<Notification> createNotifications(List<User> users, MessageTemplateType template, ScheduledTime sendTime) {
        return users.stream()
                .map(user -> createNotification(user, template, sendTime))
                .toList();
    }

    private Notification createNotification(User user, MessageTemplateType template, ScheduledTime sendTime) {
        Map<String, String> context = Map.of(USERNAME, user.getName().value());
        Message message = createMessage(template, context);
        return Notification.of(user, message, sendTime);
    }

    private Message createMessage(MessageTemplateType template, Map<String, String> context) {
        return Message.of(
                template,
                template.main(context),
                template.sub(context)
        );
    }

    private void saveNotifications(List<Notification> notifications) {
        notificationsRepository.saveAll(notifications);
    }
}
