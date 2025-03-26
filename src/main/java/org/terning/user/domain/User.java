package org.terning.user.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.terning.user.domain.vo.AccountStatus;
import org.terning.user.domain.vo.PushNotificationStatus;
import org.terning.user.domain.vo.PushToken;
import org.terning.user.domain.vo.UserName;
import org.terning.notification.domain.Notifications;
import org.terning.global.entity.BaseEntity;
import org.terning.scrap.domain.Scraps;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notifications> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Scraps> scraps = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "name"))
    private UserName name;

    @Embedded
    @AttributeOverride(name = "token", column = @Column(name = "token"))
    private PushToken token;

    @Enumerated(EnumType.STRING)
    private PushNotificationStatus pushStatus;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

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
}
