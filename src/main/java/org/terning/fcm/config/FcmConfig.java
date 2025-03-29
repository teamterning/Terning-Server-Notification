package org.terning.fcm.config;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.terning.global.constant.Encoding;
import org.terning.user.common.log.FcmLogMessages;

@Configuration
@EnableConfigurationProperties(FcmProperties.class)
@RequiredArgsConstructor
@Slf4j
public class FcmConfig {

    private final FcmProperties fcmProperties;

    @PostConstruct
    public void init() {
        try {
            String serviceKeyJson = fcmProperties.getServiceKey();

            try (ByteArrayInputStream serviceAccountStream =
                         new ByteArrayInputStream(serviceKeyJson.getBytes(Encoding.UTF_8))) {

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    log.info(FcmLogMessages.INIT_SUCCESS);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(FcmLogMessages.INIT_FAILED + e.getMessage(), e);
        }
    }
}
