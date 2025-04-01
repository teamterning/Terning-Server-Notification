package org.terning.scrap.domain;

import java.util.List;

public interface ScrapRepositoryCustom {
    List<Scrap> findScrapsByUserIds(List<Long> userIds);
}
