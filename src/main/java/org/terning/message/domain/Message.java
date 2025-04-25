package org.terning.message.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.terning.global.entity.BaseEntity;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.message.domain.enums.ViewType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageTemplateType messageTemplateType;

    private String main;

    private String sub;

    private String viewType;

    private Message(MessageTemplateType messageTemplateType, String main, String sub, String viewType) {
        this.messageTemplateType = messageTemplateType;
        this.main = main;
        this.sub = sub;
        this.viewType = viewType;
    }

    public static Message of(MessageTemplateType messageTemplateType, String main, String sub) {
        return new Message(messageTemplateType, main, sub, ViewType.fromTemplate(messageTemplateType).name());
    }

    public boolean isSameType(MessageTemplateType other) {
        return this.messageTemplateType == other;
    }
}
