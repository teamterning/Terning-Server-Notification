package org.terning.user.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepositoryCustom {
    Map<Long, User> findUsersByIds(List<Long> userIds);

    Map<Long, User> findUsersByOUserIds(List<Long> oUserIds);

    Optional<User> findByOUserId(Long oUserId);
}

