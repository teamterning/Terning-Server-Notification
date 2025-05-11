package org.terning.fcm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terning.fcm.application.PushNotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/push-notifications")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    @PostMapping("/send-all")
    public ResponseEntity<Void> sendAllPushNotifications(){
        pushNotificationService.sendAllPendingNotifications();
        return ResponseEntity.ok().build();
    }
}
