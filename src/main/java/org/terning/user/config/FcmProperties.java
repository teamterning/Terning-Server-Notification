package org.terning.user.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Component
@ConfigurationProperties(prefix = "firebase")
@Getter
@Setter
public class FcmProperties {
    private String serviceKey;
}
