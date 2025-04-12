package org.terning.user.domain;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserRepositoryTest implements UserRepository, UserRepositoryCustom {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public <S extends User> S save(S user) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);

            if (idField.get(user) == null) {
                idField.set(user, idGenerator.getAndIncrement());
                users.add(user);
            } else {
                deleteById(user.getId());
                users.add(user);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("[USER REPOSITORY TEST ERROR] User 객체의 id 필드를 설정할 수 없습니다.", e);
        }
        return user;
    }

    @Override
    public Map<Long, User> findUsersByIds(List<Long> userIds) {
        return users.stream()
                .filter(user -> userIds.contains(user.getId()))
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return users.stream().anyMatch(user -> Objects.equals(user.getId(), id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        Set<Long> idSet = new HashSet<>();
        ids.forEach(idSet::add);
        return users.stream()
                .filter(user -> idSet.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return users.size();
    }

    @Override
    public void deleteById(Long id) {
        users.removeIf(user -> Objects.equals(user.getId(), id));
    }

    @Override
    public void delete(User entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        Set<Long> idSet = new HashSet<>();
        ids.forEach(idSet::add);
        users.removeIf(user -> idSet.contains(user.getId()));
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        for (User user : entities) {
            deleteById(user.getId());
        }
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S user : entities) {
            result.add((S) save(user));
        }
        return result;
    }

    @Override
    public void flush() {}

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        deleteAllById(longs);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public User getOne(Long aLong) {
        return findById(aLong).orElse(null);
    }

    @Override
    public User getById(Long aLong) {
        return getOne(aLong);
    }

    @Override
    public User getReferenceById(Long aLong) {
        return getOne(aLong);
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return Page.empty();
    }

    @Override
    public Optional<User> findByOUserId(Long oUserId) {
        return Optional.empty();
    }
}
