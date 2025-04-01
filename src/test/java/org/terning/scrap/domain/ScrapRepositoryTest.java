package org.terning.scrap.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.terning.user.domain.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.terning.scrap.domain.QScrap.scrap;
import static org.terning.user.domain.QUser.user;

public class ScrapRepositoryTest implements ScrapRepository, ScrapRepositoryCustom {

    private final List<Scrap> scraps = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public <S extends Scrap> S save(S entity) {
        if (entity.getId() == null) {
            try {
                var idField = Scrap.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            deleteById(entity.getId());
        }
        scraps.add(entity);
        return entity;
    }

    @Override
    public List<Scrap> findScrapsByUserIds(List<Long> userIds) {
        return scraps.stream()
                .filter(scrap -> userIds.contains(scrap.getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findDistinctScrappedUsers() {
        return scraps.stream()
                .filter(scrap -> scrap.getStatus() == ScrapStatus.SCRAPPED)
                .map(Scrap::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Scrap> findAll() {
        return scraps;
    }

    @Override
    public List<Scrap> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id) {
        scraps.removeIf(scrap -> Objects.equals(scrap.getId(), id));
    }

    @Override
    public void delete(Scrap entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Scrap> entities) {

    }

    @Override
    public void deleteAll() {
        scraps.clear();
    }

    @Override
    public <S extends Scrap> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public Optional<Scrap> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Scrap> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Scrap> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Scrap> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Scrap getOne(Long aLong) {
        return null;
    }

    @Override
    public Scrap getById(Long aLong) {
        return null;
    }

    @Override
    public Scrap getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Scrap> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Scrap> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Scrap> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Scrap> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Scrap> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Scrap> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Scrap, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Scrap> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Scrap> findAll(Pageable pageable) {
        return null;
    }
}
