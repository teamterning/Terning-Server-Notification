package org.terning.scrap.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.terning.user.domain.User;
import org.terning.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "scraps")
public class Scrap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ScrapStatus status;

    private Scrap(User user) {
        this.user = user;
        this.status = ScrapStatus.SCRAPPED;
    }

    public static Scrap of(User user) {
        return new Scrap(user);
    }

    public void unscrap() {
        this.status = status.unscrap();
    }

    public void scrap() {
        this.status = status.scrap();
    }

    public boolean isScrapped() {
        return status.isScrapped();
    }

    public boolean isUnscrapped() {
        return status.isUnscrapped();
    }
}
