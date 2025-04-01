package org.terning.scrap.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.scrap.domain.*;
import org.terning.user.domain.*;
import org.terning.user.domain.vo.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ScrapSyncManager 단위 테스트")
class ScrapSyncManagerTest {

    private ScrapRepositoryTest scrapRepository;
    private UserRepositoryTest userRepository;
    private ScrapSyncManager syncManager;

    @BeforeEach
    void setUp() {
        scrapRepository = new ScrapRepositoryTest();
        userRepository = new UserRepositoryTest();
        syncManager = new ScrapSyncManager(userRepository, scrapRepository);
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("스크랩되지 않은 유저는 새로 SCRAPPED 상태로 저장된다")
        void save_new_scrap() {
            User user = saveUser("장순", "token");
            Long userId = user.getId();

            syncManager.sync(List.of(userId));

            Scrap result = scrapRepository.findScrapsByUserIds(List.of(userId)).get(0);
            assertThat(result.isScrapped()).isTrue();
        }

        @Test
        @DisplayName("UNSCRAPPED 상태인 경우 SCRAPPED로 업데이트된다")
        void update_unscrapped() {
            User user = saveUser("장순", "token");
            Scrap scrap = Scrap.of(user);
            scrap.unscrap();
            scrapRepository.save(scrap);

            syncManager.sync(List.of(user.getId()));

            Scrap result = scrapRepository.findScrapsByUserIds(List.of(user.getId())).get(0);
            assertThat(result.isScrapped()).isTrue();
        }

        @Test
        @DisplayName("이미 SCRAPPED 상태인 경우 변경되지 않는다")
        void already_scrapped() {
            User user = saveUser("장순", "token");
            scrapRepository.save(Scrap.of(user));

            syncManager.sync(List.of(user.getId()));

            List<Scrap> result = scrapRepository.findScrapsByUserIds(List.of(user.getId()));
            assertThat(result).hasSize(1);
            assertThat(result.get(0).isScrapped()).isTrue();
        }
    }

    @Nested
    @DisplayName("실패/예외 케이스")
    class Failure {

        @Test
        @DisplayName("유저가 존재하지 않으면 무시된다")
        void ignore_when_user_not_found() {
            syncManager.sync(List.of(123L));

            List<Scrap> result = scrapRepository.findScrapsByUserIds(List.of(123L));
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("중복된 유저 ID가 들어와도 한 번만 처리된다")
        void handle_duplicates_once() {
            User user = saveUser("중복유저", "token");
            Long userId = user.getId();

            syncManager.sync(List.of(userId, userId));

            List<Scrap> result = scrapRepository.findScrapsByUserIds(List.of(userId));
            assertThat(result).hasSize(1);
            assertThat(result.get(0).isScrapped()).isTrue();
        }
    }

    private User saveUser(String name, String token) {
        return userRepository.save(User.of(
                UserName.from(name),
                FcmToken.from(token),
                PushNotificationStatus.ENABLED,
                AuthType.KAKAO,
                AccountStatus.ACTIVE
        ));
    }
}