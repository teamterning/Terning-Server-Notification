package org.terning.fcm.application;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmPushScheduler {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://13.209.210.3/api/v1";
//    private static final String LOCAL_URL = "http://localhost:8081/api/v1";

    private void callPost(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = BASE_URL + path;
        try {
            restTemplate.postForEntity(url, entity, String.class);
            log.info("POST 요청 성공: {}", url);
        } catch (Exception e) {
            log.error("POST 요청 실패: {}", url, e);
        }
    }

    private void callPost(String path, String template) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> bM = new HashMap<>();
        bM.put("template", template);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(bM, headers);
        String url = BASE_URL + path;

        try {
            restTemplate.postForEntity(url, entity, String.class);
            log.info("POST 요청 성공: {}", url);
        } catch (Exception e) {
            log.error("POST 요청 실패: {}", url, e);
        }
    }

    private void callGet(String path) {
        String url = BASE_URL + path;
        try {
            restTemplate.getForEntity(url, String.class);
            log.info("GET 요청 성공: {}", url);
        } catch (Exception e) {
            log.error("GET 요청 실패: {}", url, e);
        }
    }

    // 1. 매주 월/수 16:58 스크랩 동기화
    @Scheduled(cron = "0 58 16 ? * MON,WED", zone = "Asia/Seoul")
    public void syncScraps() {
        callGet("/external/scraps/sync");
    }

    // 1-2. 매주 월/수 17:00 관심공고 알림 생성 + 전송
    @Scheduled(cron = "0 0 17 ? * MON,WED", zone = "Asia/Seoul")
    public void sendInterestedAnnouncementReminder() {
        callPost("/notification/create", "INTERESTED_ANNOUNCEMENT_REMINDER");
        callPost("/push-notifications/send-all");
    }

    // 2. 매주 목/토 13:00 최근 공고 알림 생성 + 전송
    @Scheduled(cron = "0 0 13 ? * THU,SAT", zone = "Asia/Seoul")
    public void sendRecentlyPostedInternshipRecommendation() {
        callPost("/notification/create", "RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION");
        callPost("/push-notifications/send-all");
    }

    // 3. 매주 일요일 21:00 인기 공고 알림 생성 + 전송
    @Scheduled(cron = "0 0 21 ? * SUN", zone = "Asia/Seoul")
    public void sendTrendingInternshipAlert() {
        callPost("/notification/create", "TRENDING_INTERNSHIP_ALERT");
        callPost("/push-notifications/send-all");
    }

    // 테스트용
//    @Scheduled(cron = "0 34 3 * * ?", zone = "Asia/Seoul")
//    public void testPush() {
//        log.info("테스트 푸시 시작");
//
//        callPost("/notification/create", "RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION");
//        callPost("/push-notifications/send-all");
//
//        log.info("테스트 푸시 완료");
//    }
}
