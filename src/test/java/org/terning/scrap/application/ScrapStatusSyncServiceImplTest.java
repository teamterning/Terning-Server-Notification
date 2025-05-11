package org.terning.scrap.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.scrap.config.OpsScrapUserClient;
import org.terning.scrap.dto.response.OpsScrapUserResponse;
import org.terning.scrap.dto.response.OpsScrapUsersResponse;
import org.terning.scrap.domain.Scrap;
import org.terning.scrap.domain.ScrapRepositoryTest;
import org.terning.user.domain.UserRepositoryTest;
import org.terning.user.domain.User;
import org.terning.user.domain.vo.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("스크랩 상태 동기화 서비스 테스트")
class ScrapStatusSyncServiceImplTest {

    private ScrapRepositoryTest scrapRepository;
    private UserRepositoryTest userRepository;
    private FakeOpsScrapUserClient fakeClient;
    private ScrapStatusSyncServiceImpl service;

    @BeforeEach
    void setUp() {
        scrapRepository = new ScrapRepositoryTest();
        userRepository = new UserRepositoryTest();
        fakeClient = new FakeOpsScrapUserClient();
        ScrapSyncManager syncManager = new ScrapSyncManager(userRepository, scrapRepository);
        service = new ScrapStatusSyncServiceImpl(fakeClient, syncManager);
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("스크랩되지 않은 유저를 동기화한다")
        void sync_new_scrap_users() {
            User user = saveUser("장순", "token");
            fakeClient.setUserIds(List.of(user.getId()));

            service.syncScrapUsersFromOps();

            assertThat(scrapRepository.findScrapsByUserIds(List.of(user.getId()))).hasSize(1);
            assertThat(scrapRepository.findScrapsByUserIds(List.of(user.getId())).get(0).isScrapped()).isTrue();
        }

        @Test
        @DisplayName("스크랩이 이미 된 유저는 건드리지 않는다")
        void already_scrapped_user_is_ignored() {
            User user = saveUser("장순", "token");
            scrapRepository.save(Scrap.of(user));
            fakeClient.setUserIds(List.of(user.getId()));

            service.syncScrapUsersFromOps();

            assertThat(scrapRepository.findScrapsByUserIds(List.of(user.getId()))).hasSize(1);
        }
    }

    @Nested
    @DisplayName("실패 및 예외 케이스")
    class Failure {

        @Test
        @DisplayName("운영 서버에서 유저가 없을 경우 아무 작업도 하지 않는다")
        void skip_when_no_user_from_ops() {
            fakeClient.setUserIds(List.of());

            service.syncScrapUsersFromOps();

            assertThat(scrapRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("운영 서버에서 전달된 유저가 존재하지 않는 경우 무시한다")
        void skip_when_user_not_found() {
            fakeClient.setUserIds(List.of(999L));

            service.syncScrapUsersFromOps();

            assertThat(scrapRepository.findAll()).isEmpty();
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

    static class FakeOpsScrapUserClient extends OpsScrapUserClient {
        private List<Long> userIds;

        public FakeOpsScrapUserClient() {
            super(null);
        }

        public void setUserIds(List<Long> userIds) {
            this.userIds = userIds;
        }

        @Override
        public OpsScrapUsersResponse fetchUnsyncedUsers() {
            List<OpsScrapUserResponse> responses = userIds.stream()
                    .map(OpsScrapUserResponse::from)
                    .toList();
            return new OpsScrapUsersResponse(responses);
        }
    }
}