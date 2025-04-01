package org.terning.notification.domain;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NotificationsRepositoryTest implements NotificationsRepository {

    private final List<Notification> notifications = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public <S extends Notification> S save(S entity) {
        if (entity.getId() == null) {
            try {
                Field idField = Notification.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, idGenerator.getAndIncrement());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            deleteById(entity.getId());
        }
        notifications.add(entity);
        return entity;
    }

    @Override
    public <S extends Notification> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notifications.stream()
                .filter(notification -> Objects.equals(notification.getId(), id))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return notifications.stream()
                .anyMatch(notification -> Objects.equals(notification.getId(), id));
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(notifications);
    }

    @Override
    public List<Notification> findAllById(Iterable<Long> ids) {
        Set<Long> idSet = new HashSet<>();
        ids.forEach(idSet::add);
        return notifications.stream()
                .filter(notification -> idSet.contains(notification.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return notifications.size();
    }

    @Override
    public void deleteById(Long id) {
        notifications.removeIf(notification -> Objects.equals(notification.getId(), id));
    }

    @Override
    public void delete(Notification entity) {
        notifications.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        Set<Long> idSet = new HashSet<>();
        ids.forEach(idSet::add);
        notifications.removeIf(notification -> idSet.contains(notification.getId()));
    }

    @Override
    public void deleteAll(Iterable<? extends Notification> entities) {
        for (Notification entity : entities) {
            notifications.remove(entity);
        }
    }

    @Override
    public void deleteAll() {
        notifications.clear();
    }

    /* 이하 JpaRepository 디폴트 메서드들 구현 (필요 시 수정/확장) */

    @Override
    public void flush() {
        // 메모리 구현에서는 별도 처리 없음
    }

    @Override
    public <S extends Notification> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends Notification> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch() {
        notifications.clear();
    }

    @Override
    public void deleteAllInBatch(Iterable<Notification> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        deleteAllById(ids);
    }

    @Override
    public Notification getOne(Long aLong) {
        return findById(aLong).orElse(null);
    }

    @Override
    public Notification getById(Long aLong) {
        return findById(aLong).orElse(null);
    }

    @Override
    public Notification getReferenceById(Long aLong) {
        return getById(aLong);
    }

    @Override
    public <S extends Notification> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Notification> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Notification> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Notification> Page<S> findAll(Example<S> example, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public <S extends Notification> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Notification> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Notification, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Notification> findAll(Sort sort) {
        return notifications;
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), notifications.size());
        List<Notification> content = notifications.subList(start, end);
        return new PageImpl<>(content, pageable, notifications.size());
    }
}
