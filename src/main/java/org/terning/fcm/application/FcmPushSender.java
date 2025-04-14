package org.terning.fcm.application;

import org.terning.notification.domain.Notification;

public interface FcmPushSender {

    boolean send(Notification notification);
}
