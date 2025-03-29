package org.terning.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FcmConfig {

    private final FcmProperties fcmProperties;

    @PostConstruct
    public void init() {
        try {
            // 환경변수에서 주입된 JSON 가져오기
            String serviceKeyJson = fcmProperties.getServiceKey();

            // Json 문자열 > InputStream 으로 변환
            try (ByteArrayInputStream serviceAccountStream =
                         new ByteArrayInputStream(serviceKeyJson.getBytes(StandardCharsets.UTF_8))) {

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    log.info("✅ Firebase 초기화 성공");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Firebase 초기화 실패: " + e.getMessage(), e);
        }
    }
}
