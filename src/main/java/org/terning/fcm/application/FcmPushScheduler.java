package org.terning.fcm.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.terning.notification.application.NotificationService;
import org.terning.notification.domain.vo.ScheduledTime;
import org.terning.notification.dto.request.NotificationCreateRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmPushScheduler {

    private final RestTemplate restTemplate;
    private final NotificationService notificationService;
    private final PushNotificationService pushNotificationService;

    @Value("${ops.server.url}/api/v1")
    private String base_url;

    // 1. 매주 월/수 16:58 스크랩 동기화
    @Scheduled(cron = "0 58 16 ? * MON,WED", zone = "Asia/Seoul")
    public void syncScraps() {
        log.info("운영 서버 스크랩 동기화를 요청합니다.");
        callGet("/external/scraps/sync");
    }

    // 1-2. 매주 월/수 17:00 관심공고 알림 생성 + 전송
    @Scheduled(cron = "0 0 17 ? * MON,WED", zone = "Asia/Seoul")
    public void sendInterestedAnnouncementReminder() {
        log.info("관심 공고 알림 생성을 시작합니다.");
        createAndSendNotifications("INTERESTED_ANNOUNCEMENT_REMINDER");
    }

    // 2. 매주 목/토 13:00 최근 공고 알림 생성 + 전송
    @Scheduled(cron = "0 0 13 ? * THU,SAT", zone = "Asia/Seoul")
    public void sendRecentlyPostedInternshipRecommendation() {
        log.info("최근 공고 알림 생성을 시작합니다.");
        createAndSendNotifications("RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION");
    }

    // 3. 매주 일요일 21:00 인기 공고 알림 생성 + 전송
    @Scheduled(cron = "0 0 21 ? * SUN", zone = "Asia/Seoul")
    public void sendTrendingInternshipAlert() {
        log.info("인기 공고 알림 생성을 시작합니다.");
        createAndSendNotifications("TRENDING_INTERNSHIP_ALERT");
    }

    private void createAndSendNotifications(String template) {
        try {
            NotificationCreateRequest request = new NotificationCreateRequest(template);
            ScheduledTime scheduledTime = ScheduledTime.of(LocalDateTime.now());
            notificationService.createNotification(request, scheduledTime);
            log.info("'{}' 템플릿의 알림이 생성되었습니다.", template);

            pushNotificationService.sendAllPendingNotifications();
            log.info("생성된 알림에 대한 푸시 전송을 시작합니다.");
        } catch (Exception e) {
            log.error("'{}' 템플릿 알림 처리 중 내부 오류 발생", template, e);
        }
    }

    private void callGet(String path) {
        String fullUrl = base_url + path;
        try {
            restTemplate.getForEntity(fullUrl, String.class);
            log.info("운영 서버 GET 요청 성공: {}", fullUrl);
        } catch (Exception e) {
            log.error("운영 서버 GET 요청 실패: {}", fullUrl, e);
        }
    }
}
