package org.terning.user.domain;

import java.util.List;
import java.util.Map;

public interface UserRepositoryCustom {
    Map<Long, User> findUsersByIds(List<Long> userIds);
}

