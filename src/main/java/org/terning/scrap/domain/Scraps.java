package org.terning.scrap.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scraps {

    private final List<Scrap> scraps;

    private Scraps(List<Scrap> scraps) {
        this.scraps = List.copyOf(scraps);
    }

    public static Scraps of(List<Scrap> scraps) {
        return new Scraps(scraps);
    }

    public static Scraps empty() {
        return new Scraps(Collections.emptyList());
    }

    public List<Scrap> values() {
        return Collections.unmodifiableList(scraps);
    }

    public Scraps add(Scrap scrap) {
        List<Scrap> newList = new ArrayList<>(this.scraps);
        newList.add(scrap);
        return new Scraps(newList);
    }

    public Scraps remove(Scrap scrap) {
        List<Scrap> newList = new ArrayList<>(this.scraps);
        newList.remove(scrap);
        return new Scraps(newList);
    }
}
