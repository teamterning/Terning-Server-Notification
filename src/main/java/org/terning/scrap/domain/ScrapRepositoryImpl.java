package org.terning.scrap.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.terning.user.domain.User;

import static org.terning.user.domain.QUser.user;


import java.util.List;

import static org.terning.scrap.domain.QScrap.scrap;

@Repository
@RequiredArgsConstructor
public class ScrapRepositoryImpl implements ScrapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Scrap> findScrapsByUserIds(List<Long> userIds) {
        return queryFactory
                .selectFrom(scrap)
                .where(scrap.user.id.in(userIds))
                .fetch();
    }

    @Override
    public List<User> findDistinctScrappedUsers() {
        return queryFactory
                .selectDistinct(scrap.user)
                .from(scrap)
                .join(scrap.user, user).fetchJoin()
                .where(scrap.status.eq(ScrapStatus.SCRAPPED))
                .fetch();
    }
}

