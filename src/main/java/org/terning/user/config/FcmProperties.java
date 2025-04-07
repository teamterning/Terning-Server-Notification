package org.terning.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "firebase")
@Getter
@Setter
public class FcmProperties {
    private String serviceKey;
}
