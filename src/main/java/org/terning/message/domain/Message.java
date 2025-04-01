package org.terning.message.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.terning.global.entity.BaseEntity;
import org.terning.message.domain.enums.MessageTemplateType;

import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageTemplateType messageTemplateType;

    private String formattedMainMessage;
    private String formattedSubMessage;

    private Message(MessageTemplateType template, String main, String sub) {
        this.messageTemplateType = template;
        this.formattedMainMessage = main;
        this.formattedSubMessage = sub;
    }

    public static Message of(MessageTemplateType template, Map<String, String> params) {
        String main = template.main(params);
        String sub = template.sub(params);
        return new Message(template, main, sub);
    }

    public boolean isSameType(MessageTemplateType otherTemplate) {
        return this.messageTemplateType == otherTemplate;
    }
}

