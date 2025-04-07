package org.terning.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmPropertyTester implements CommandLineRunner {

    private final FcmProperties fcmProperties;

    @Override
    public void run(String... args) {
        System.out.println("🔥 FCM KEY: " + fcmProperties.getServiceKeyPath());
    }
}
