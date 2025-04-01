package org.terning.user.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, User> findUsersByIds(List<Long> userIds) {
        return queryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.id.in(userIds))
                .fetch()
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }
}

