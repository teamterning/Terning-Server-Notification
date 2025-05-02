package org.terning.notification.application.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class NotificationWriterImpl implements NotificationWriter {

    private static final String USERNAME = "username";

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final NotificationsRepository notificationsRepository;

    @Override
    @Transactional
    public void write(MessageTemplateType template, ScheduledTime sendTime) {
        log.info("🔔 [NotificationWriter] 알림 생성 시작 - 템플릿: {}", template);
        List<User> targetUsers = fetchTargetUsers(template.targetType());
        log.info("👥 [NotificationWriter] 대상 유저 수 (푸시 수신 허용 + 토큰 보유): {}", targetUsers.size());
//        List<User> filteredUsers = targetUsers.stream()
//                .filter(user -> user.getToken() != null)
//                .filter(user -> user.getToken().value() != null)
//                .filter(user -> !user.getToken().value().trim().isEmpty())
//                .toList();
        List<User> filteredUsers = targetUsers.stream()
                .filter(user -> {
                    boolean valid = user.getToken() != null && user.getToken().value() != null && !user.getToken().value().trim().isEmpty();
                    if (!valid) {
                        log.warn("⚠️ [NotificationWriter] FCM 토큰 누락 - user(oUserId={}, name={})", user.getOUserId(), user.getName() != null ? user.getName().value() : "null");
                    }
                    return valid;
                })
                .toList();
        log.info("✅ [NotificationWriter] 최종 알림 대상 유저 수: {}", filteredUsers.size());

        List<Notification> notifications = createNotifications(filteredUsers, template, sendTime);
        log.info("💾 [NotificationWriter] 생성된 알림 수: {}", notifications.size());

        saveNotifications(notifications);
        log.info("🎉 [NotificationWriter] 알림 저장 완료");
    }

    private List<User> fetchTargetUsers(MessageTargetType targetType) {
        log.info("🔍 [fetchTargetUsers] 대상 타입: {}", targetType);
//        List<User> users = switch (targetType) {
//            case SCRAPPED_USER -> scrapRepository.findDistinctScrappedUsers();
//            case ALL_USERS -> userRepository.findAll();
//            default -> throw new NotificationException(NotificationErrorCode.INVALID_TARGET_TYPE);
//        };
//        return users.stream()
//                .filter(User::canReceivePushNotification)
//                .toList();
        List<User> users;

        try {
            users = switch (targetType) {
                case SCRAPPED_USER -> {
                    List<User> scrappedUsers = scrapRepository.findDistinctScrappedUsers();
                    log.info("📦 [fetchTargetUsers] 스크랩 유저 수: {}", scrappedUsers.size());
                    yield scrappedUsers;
                }
                case ALL_USERS -> {
                    List<User> allUsers = userRepository.findAll();
                    log.info("📦 [fetchTargetUsers] 전체 유저 수: {}", allUsers.size());
                    yield allUsers;
                }
                default -> throw new NotificationException(NotificationErrorCode.INVALID_TARGET_TYPE);
            };
        } catch (Exception e) {
            log.error("🔥 [fetchTargetUsers] 유저 조회 중 예외 발생", e);
            throw e;
        }

        return users.stream()
                .peek(user -> {
                    if (user.getPushStatus() == null) {
                        log.warn("⚠️ [fetchTargetUsers] 유저(oUserId={})의 pushStatus가 null", user.getOUserId());
                    }
                    if (user.getToken() == null) {
                        log.warn("⚠️ [fetchTargetUsers] 유저(oUserId={})의 FCM 토큰이 null", user.getOUserId());
                    }
                })
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
                template.sub(context),
                template.getImageUrl()
        );
    }

    private void saveNotifications(List<Notification> notifications) {
        notificationsRepository.saveAll(notifications);
    }
}
