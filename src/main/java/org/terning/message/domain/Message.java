package org.terning.message.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.terning.global.entity.BaseEntity;
import org.terning.message.domain.enums.MessageTemplateType;

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

    private Message(MessageTemplateType messageTemplateType, String formattedMainMessage, String formattedSubMessage) {
        this.messageTemplateType = messageTemplateType;
        this.formattedMainMessage = formattedMainMessage;
        this.formattedSubMessage = formattedSubMessage;
    }

    public static Message of(MessageTemplateType template, String formattedMainMessage, String formattedSubMessage) {
        return new Message(template, formattedMainMessage, formattedSubMessage);
    }

    public boolean isSameType(MessageTemplateType otherTemplate) {
        return this.messageTemplateType == otherTemplate;
    }
}
