package org.terning.user.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
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

    @Override
    public Map<Long, User> findUsersByOUserIds(List<Long> oUserIds) {
        return queryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.oUserId.in(oUserIds))
                .fetch()
                .stream()
                .collect(Collectors.toMap(User::getOUserId, Function.identity()));
    }

    @Override
    public Optional<User> findByOUserId(Long oUserId) {
        QUser user = QUser.user;
        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(user.oUserId.eq(oUserId))
                        .fetchOne()
        );
    }

}

