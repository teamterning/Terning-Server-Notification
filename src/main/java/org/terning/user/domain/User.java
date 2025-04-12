package org.terning.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.terning.fcm.validate.FcmTokenValidator;
import org.terning.global.entity.BaseEntity;
import org.terning.notification.domain.Notification;
import org.terning.notification.domain.Notifications;
import org.terning.scrap.domain.Scrap;
import org.terning.scrap.domain.Scraps;
import org.terning.user.domain.vo.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Scrap> scraps = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long oUserId;

    @Setter
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private UserName name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "token"))
    private FcmToken token;

    @Setter
    @Enumerated(EnumType.STRING)
    private PushNotificationStatus pushStatus;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private User(Long oUserId, UserName name, FcmToken token, PushNotificationStatus pushStatus, AuthType authType, AccountStatus accountStatus) {
        this.oUserId = oUserId;
        this.name = name;
        this.token = token;
        this.pushStatus = pushStatus;
        this.authType = authType;
        this.accountStatus = accountStatus;
    }

    public static User of(Long oUserId, UserName name, FcmToken token, PushNotificationStatus pushStatus, AuthType authType, AccountStatus accountStatus) {
        return new User(oUserId, name, token, pushStatus, authType, accountStatus);
    }

    public Notifications notifications() {
        return Notifications.of(this.notifications);
    }

    public Scraps scraps() {
        return Scraps.of(this.scraps);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
    }

    public void addScrap(Scrap scrap) {
        scraps.add(scrap);
    }

    public void removeScrap(Scrap scrap) {
        scraps.remove(scrap);
    }

    public boolean canReceivePushNotification() {
        return pushStatus.canReceiveNotification();
    }

    public void enablePushNotification() {
        this.pushStatus = pushStatus.enable();
    }

    public void disablePushNotification() {
        this.pushStatus = pushStatus.disable();
    }

    public boolean isActiveUser() {
        return !accountStatus.isWithdrawn();
    }

    public boolean isFcmTokenExpired(FcmTokenValidator validator) {
        return this.token.isExpiredWith(validator);
    }

    public void updateFcmToken(String newTokenValue) {
        this.token = token.updateValue(newTokenValue);
    }

}
