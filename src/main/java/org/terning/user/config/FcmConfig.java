package org.terning.user.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.terning.global.constant.Encoding;
import org.terning.user.common.log.FcmLogMessages;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FcmConfig {

    private final FcmProperties fcmProperties;

    @PostConstruct
    public void init() {
        try {
            String base64EncodedServiceKey = fcmProperties.getServiceKey();
    
            if (base64EncodedServiceKey == null || base64EncodedServiceKey.isBlank()) {
                throw new IllegalStateException("Firebase service key is missing or blank.");
            }
    
            byte[] decoded = java.util.Base64.getDecoder().decode(base64EncodedServiceKey);
    
            try (ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(decoded)) {
    
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
