package org.terning.user.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.terning.fcm.validate.FcmTokenValidator;
import org.terning.user.domain.vo.*;
import org.terning.notification.domain.Notifications;
import org.terning.global.entity.BaseEntity;
import org.terning.scrap.domain.Scrap;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notifications> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Scrap> scraps = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long oUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private UserName name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "token"))
    private FcmToken token;

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
}
