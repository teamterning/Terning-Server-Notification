<<<<<<<< HEAD:src/main/java/org/terning/user/User.java
package org.terning.user;

import jakarta.persistence.*;
========
package org.terning.user.domain;
>>>>>>>> develop:src/main/java/org/terning/user/domain/User.java

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
<<<<<<<< HEAD:src/main/java/org/terning/user/User.java
import org.terning.domain.Notifications;
import org.terning.domain.Scraps;
import org.terning.domain.common.BaseEntity;
import org.terning.domain.enums.AuthType;
import org.terning.domain.enums.State;
import org.terning.user.vo.UserName;
========
import org.terning.notification.domain.Notifications;
import org.terning.global.entity.BaseEntity;
import org.terning.scrap.domain.Scraps;
>>>>>>>> develop:src/main/java/org/terning/user/domain/User.java

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

    private String token;

    private boolean isPushEnable;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private State state;
}
