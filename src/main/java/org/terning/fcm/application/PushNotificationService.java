package org.terning.fcm.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terning.notification.domain.Notification;
import org.terning.notification.domain.NotificationsRepository;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final NotificationsRepository notificationsRepository;
    private final FcmPushSender fcmPushSender;

    @Transactional
    public void sendAllPendingNotifications() {
        List<Notification> pendingNotifications = notificationsRepository.findAll().stream()
                .filter(notification -> !notification.isSent() &&
                        notification.isDue(LocalDateTime.now()))
                .toList();

        for (Notification notification : pendingNotifications) {
            boolean sent = fcmPushSender.send(notification);
            if (sent) {
                notification.markSent();
            }
            //TODO : 실패한 경우 예외처리 추가
        }
        notificationsRepository.saveAll(pendingNotifications);
    }
}
