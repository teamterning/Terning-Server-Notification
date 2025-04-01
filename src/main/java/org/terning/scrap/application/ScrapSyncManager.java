package org.terning.scrap.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.terning.scrap.domain.Scrap;
import org.terning.scrap.domain.ScrapRepository;
import org.terning.user.domain.User;
import org.terning.user.domain.UserRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScrapSyncManager {

    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;

    public void sync(List<Long> userIds) {
        List<Long> distinctUserIds = userIds.stream().distinct().toList();

        Map<Long, User> usersById = userRepository.findUsersByIds(distinctUserIds);
        Map<Long, Scrap> scrapsByUserId = scrapRepository.findScrapsByUserIds(distinctUserIds).stream()
                .collect(Collectors.toMap(scrap -> scrap.getUser().getId(), Function.identity()));

        List<Scrap> scrapsToPersist = distinctUserIds.stream()
                .map(userId -> createOrUpdateScrap(userId, usersById, scrapsByUserId))
                .filter(Objects::nonNull)
                .toList();

        if (!scrapsToPersist.isEmpty()) {
            scrapRepository.saveAll(scrapsToPersist);
        }
    }

    private Scrap createOrUpdateScrap(
            Long userId,
            Map<Long, User> usersById,
            Map<Long, Scrap> scrapsByUserId
    ) {
        User user = usersById.get(userId);
        if (user == null) {
            return null;
        }

        Scrap existingScrap = scrapsByUserId.get(userId);
        if (existingScrap == null) {
            return Scrap.of(user);
        }

        if (existingScrap.isUnscrapped()) {
            existingScrap.scrap();
            return existingScrap;
        }

        return null;
    }
}