package org.terning.fcm.application;

import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.terning.message.domain.enums.ViewType;
import org.terning.notification.domain.Notification;
import org.terning.user.domain.User;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmPushSenderImpl implements FcmPushSender {

    @Override
    public boolean send(Notification notification) {
        try {
            User user = notification.getUser();
            String token = user.getToken().value();

            String title = notification.getMessage().getMain();
            String body = notification.getMessage().getSub();
            String type = notification.getMessage().getViewType();
            String imageUrl = notification.getMessage().getImageUrl();

            ApsAlert apsAlert = ApsAlert.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Aps aps = Aps.builder()
                    .setAlert(apsAlert)
                    .setMutableContent(true)
                    .putCustomData("type", type)
                    .putCustomData("imageUrl", imageUrl)
                    .build();

            ApnsConfig apnsConfig = ApnsConfig.builder()
                    .setAps(aps)
                    .build();

            Message fcmMessage = Message.builder()
                    .setToken(token)
                    .setApnsConfig(apnsConfig)
                    .putData("title", title)
                    .putData("body", body)
                    .putData("type", type)
                    .putData("imageUrl", imageUrl)
                    .build();

            log.info("📦 전송된 FCM 메시지: {}", fcmMessage);

            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            log.info("FCM 메세지 전송 성공, 응답 ID: {}", response);
            return true;
        } catch (FirebaseMessagingException e) {
            log.error("FCM 메세지 전송 실패: {}", e.getMessage(), e);
            return false;
        }
    }
}
