package org.terning.scrap.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}

