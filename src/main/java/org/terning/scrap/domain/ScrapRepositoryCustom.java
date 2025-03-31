package org.terning.scrap.domain;

import org.terning.user.domain.User;

import java.util.List;

public interface ScrapRepositoryCustom {
    List<Scrap> findScrapsByUserIds(List<Long> userIds);
    List<User> findDistinctScrappedUsers();
}
