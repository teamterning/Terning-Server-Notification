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

    public void sync(List<Long> oUserIds) {
        List<Long> distinctOUserIds = oUserIds.stream().distinct().toList();
        Map<Long, User> usersByOUserId = userRepository.findUsersByOUserIds(distinctOUserIds);

        List<Long> internalUserIds = usersByOUserId.values().stream()
                .map(User::getId)
                .toList();

        Map<Long, Scrap> scrapsByUserId = scrapRepository.findScrapsByUserIds(internalUserIds).stream()
                .collect(Collectors.toMap(scrap -> scrap.getUser().getId(), Function.identity()));

        List<Scrap> scrapsToPersist = distinctOUserIds.stream()
                .map(oUserId -> {
                    User user = usersByOUserId.get(oUserId);
                    if (user == null) return null;

                    Scrap existingScrap = scrapsByUserId.get(user.getId());
                    if (existingScrap == null) return Scrap.of(user);
                    if (existingScrap.isUnscrapped()) {
                        existingScrap.scrap();
                        return existingScrap;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        if (!scrapsToPersist.isEmpty()) {
            scrapRepository.saveAll(scrapsToPersist);
        }
    }
}