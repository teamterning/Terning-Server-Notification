package org.terning.scrap.batch.step;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.terning.scrap.dto.response.OpsScrapUserResponse;
import org.terning.scrap.domain.*;
import org.terning.scrap.application.ScrapSyncManager;
import org.terning.user.domain.*;
import org.terning.user.domain.vo.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("스크랩 동기화 배치 Writer 테스트")
class ScrapSyncWriterTest {

    private ScrapRepositoryTest scrapRepository;
    private UserRepositoryTest userRepository;
    private ScrapSyncWriter writer;

    @BeforeEach
    void setUp() {
        scrapRepository = new ScrapRepositoryTest();
        userRepository = new UserRepositoryTest();
        ScrapSyncManager scrapSyncManager = new ScrapSyncManager(userRepository, scrapRepository);
        writer = new ScrapSyncWriter(scrapSyncManager);
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("스크랩되지 않은 유저를 새로 스크랩한다")
        void save_new_scrap() {
            User user = saveUser("장순", "a");
            Long userId = user.getId();

            Chunk<OpsScrapUserResponse> chunk = new Chunk<>(List.of(OpsScrapUserResponse.from(userId)));

            writer.write(chunk);

            Scrap saved = scrapRepository.findScrapsByUserIds(List.of(userId)).get(0);
            assertThat(saved.isScrapped()).isTrue();
        }

        @Test
        @DisplayName("UNSCRAPPED 상태의 스크랩을 SCRAPPED로 변경한다")
        void update_scrap_status() {
            User user = saveUser("장순", "a");
            Scrap scrap = Scrap.of(user);
            scrap.unscrap();
            scrapRepository.save(scrap);

            Chunk<OpsScrapUserResponse> chunk = new Chunk<>(List.of(OpsScrapUserResponse.from(user.getId())));

            writer.write(chunk);

            Scrap updated = scrapRepository.findScrapsByUserIds(List.of(user.getId())).get(0);
            assertThat(updated.isScrapped()).isTrue();
        }

        @Test
        @DisplayName("이미 SCRAPPED 상태인 경우는 저장하지 않는다")
        void already_scrapped_should_be_ignored() {
            User user = saveUser("장순", "a");
            scrapRepository.save(Scrap.of(user));

            Chunk<OpsScrapUserResponse> chunk = new Chunk<>(List.of(OpsScrapUserResponse.from(user.getId())));

            writer.write(chunk);

            List<Scrap> scraps = scrapRepository.findScrapsByUserIds(List.of(user.getId()));
            assertThat(scraps).hasSize(1);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class Failure {

        @Test
        @DisplayName("존재하지 않는 유저 ID는 무시한다")
        void ignore_if_user_not_found() {
            Long unknownUserId = 999L;

            Chunk<OpsScrapUserResponse> chunk = new Chunk<>(List.of(OpsScrapUserResponse.from(unknownUserId)));

            writer.write(chunk);

            List<Scrap> result = scrapRepository.findScrapsByUserIds(List.of(unknownUserId));
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("중복된 유저 ID가 들어와도 한 번만 처리된다")
        void process_only_once_when_duplicated() {
            User user = saveUser("중복유저", "token");
            Long userId = user.getId();

            Chunk<OpsScrapUserResponse> chunk = new Chunk<>(List.of(
                    OpsScrapUserResponse.from(userId),
                    OpsScrapUserResponse.from(userId)
            ));

            writer.write(chunk);

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
