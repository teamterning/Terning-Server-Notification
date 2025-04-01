package org.terning.notification.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terning.notification.application.NotificationService;
import org.terning.notification.dto.request.NotificationCreateRequest;
import org.terning.notification.domain.vo.ScheduledTime;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody NotificationCreateRequest request) {
        ScheduledTime scheduledTime = ScheduledTime.of(LocalDateTime.now());
        notificationService.createNotification(request, scheduledTime);
        return ResponseEntity.ok().build();
    }
}