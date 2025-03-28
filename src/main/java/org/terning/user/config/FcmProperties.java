package org.terning.user.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "firebase")
@Getter
public class FcmProperties {

    private final String serviceKey;

    @ConstructorBinding
    public FcmProperties(String serviceKey){
        this.serviceKey = serviceKey;
    }
}
