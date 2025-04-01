package org.terning.notification.application.writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.terning.message.domain.enums.MessageTemplateType;
import org.terning.notification.domain.Notification;
import org.terning.notification.domain.NotificationsRepositoryTest;
import org.terning.notification.domain.vo.ScheduledTime;
import org.terning.scrap.domain.Scrap;
import org.terning.scrap.domain.ScrapRepositoryTest;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepositoryTest;
import org.terning.user.domain.vo.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("알림 작성 서비스(NotificationWriterImpl) 테스트")
class NotificationWriterImplTest {

    private ScrapRepositoryTest scrapRepository;
    private UserRepositoryTest userRepository;
    private NotificationsRepositoryTest notificationsRepository;
    private NotificationWriterImpl notificationWriter;

    @BeforeEach
    void setUp() {
        scrapRepository = new ScrapRepositoryTest();
        userRepository = new UserRepositoryTest();
        notificationsRepository = new NotificationsRepositoryTest();

        notificationWriter = new NotificationWriterImpl(
                scrapRepository,
                userRepository,
                notificationsRepository
        );
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("모든 유저에게 '최근 인턴십 추천' 알림 생성")
        void createRecentInternshipNotificationForAllUsers() {
            // Given
            User jayson = saveUser("장순", "token1");
            User dragon = saveUser("용태", "token2");
            MessageTemplateType template = MessageTemplateType.RECENTLY_POSTED_INTERNSHIP_RECOMMENDATION;
            LocalDateTime scheduledDateTime = LocalDateTime.now().plusHours(1);
            ScheduledTime scheduledTime = ScheduledTime.of(scheduledDateTime);

            // When
            notificationWriter.write(template, scheduledTime);

            // Then
            List<Notification> notifications = notificationsRepository.findAll();
            assertThat(notifications).hasSize(2);

            Notification JaysonNotification = notifications.stream()
                    .filter(n -> n.getUser().equals(jayson))
                    .findFirst()
                    .orElseThrow();

            assertThat(JaysonNotification.getMessage().getMain())
                    .isEqualTo(template.main(Map.of("username", "장순")));
            assertThat(JaysonNotification.getSchedule().value()).isEqualTo(scheduledDateTime);

            Notification DragonNotification = notifications.stream()
                    .filter(n -> n.getUser().equals(dragon))
                    .findFirst()
                    .orElseThrow();

            assertThat(DragonNotification.getMessage().getSub())
                    .isEqualTo(template.sub(Map.of("username", "용태")));
        }

        @Test
        @DisplayName("스크랩한 유저에게 '관심 공고 알림' 생성")
        void createInterestedAnnouncementForScrappedUsers() {
            // Given
            User jayson = saveUser("장순", "token1");
            User dragon = saveUser("용태", "token2");

            Scrap JaysonScrap = Scrap.of(jayson);
            scrapRepository.save(JaysonScrap);

            MessageTemplateType template = MessageTemplateType.INTERESTED_ANNOUNCEMENT_REMINDER;
            LocalDateTime scheduledDateTime = LocalDateTime.now().plusHours(2);
            ScheduledTime scheduledTime = ScheduledTime.of(scheduledDateTime);

            // When
            notificationWriter.write(template, scheduledTime);

            // Then
            List<Notification> notifications = notificationsRepository.findAll();
            assertThat(notifications).hasSize(1);

            Notification JaysonNotification = notifications.get(0);
            assertThat(JaysonNotification.getUser()).isEqualTo(jayson);
            assertThat(JaysonNotification.getMessage().getMain())
                    .isEqualTo(template.main(Map.of("username", "장순")));
            assertThat(JaysonNotification.getMessage().getSub())
                    .isEqualTo(template.sub(Map.of("username", "장순")));
            assertThat(JaysonNotification.getSchedule().value()).isEqualTo(scheduledDateTime);

            // 용태에는 알림이 없는지 확인
            boolean DragonHasNotification = notifications.stream()
                    .anyMatch(n -> n.getUser().equals(dragon));
            assertThat(DragonHasNotification).isFalse();
        }
    }

    @Nested
    @DisplayName("실패 및 예외 케이스")
    class Failure {

        @Test
        @DisplayName("스크랩하지 않은 유저에게는 알림이 생성되지 않는다")
        void noNotificationForNonScrappedUser() {
            // Given
            User jayson = saveUser("장순", "token1");
            User dragon = saveUser("용태", "token2");

            Scrap JaysonScrap = Scrap.of(jayson);
            scrapRepository.save(JaysonScrap);

            MessageTemplateType template = MessageTemplateType.INTERESTED_ANNOUNCEMENT_REMINDER;
            LocalDateTime scheduledDateTime = LocalDateTime.now().plusHours(2);
            ScheduledTime sendTime = ScheduledTime.of(scheduledDateTime);

            // When
            notificationWriter.write(template, sendTime);

            // Then
            List<Notification> notifications = notificationsRepository.findAll();
            assertThat(notifications).hasSize(1);

            Notification JaysonNotification = notifications.get(0);
            assertThat(JaysonNotification.getUser()).isEqualTo(jayson);
            assertThat(JaysonNotification.getMessage().getMain())
                    .isEqualTo(template.main(Map.of("username", "장순")));
            assertThat(JaysonNotification.getMessage().getSub())
                    .isEqualTo(template.sub(Map.of("username", "장순")));
            assertThat(JaysonNotification.getSchedule().value()).isEqualTo(scheduledDateTime);

            boolean DragonHasNotification = notifications.stream()
                    .anyMatch(n -> n.getUser().equals(dragon));
            assertThat(DragonHasNotification).isFalse();
        }

        @Test
        @DisplayName("유저가 없는 경우에는 알림이 생성되지 않는다")
        void noNotificationsWhenNoUsers() {
            // Given
            MessageTemplateType template = MessageTemplateType.TRENDING_INTERNSHIP_ALERT;
            LocalDateTime scheduledDateTime = LocalDateTime.now().plusHours(1);
            ScheduledTime sendTime = ScheduledTime.of(scheduledDateTime);

            // When
            notificationWriter.write(template, sendTime);

            // Then
            assertThat(notificationsRepository.findAll()).isEmpty();
        }
    }

    private User saveUser(String name, String token) {
        return userRepository.save(
                User.of(
                        UserName.from(name),
                        FcmToken.from(token),
                        PushNotificationStatus.ENABLED,
                        AuthType.KAKAO,
                        AccountStatus.ACTIVE
                )
        );
    }
}

