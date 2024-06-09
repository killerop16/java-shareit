package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.modelException.DuplicateEmailException;
import ru.practicum.shareit.exception.modelException.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @Override
    public User create(User user) {
        if (isDuplicateEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        user.setId(generateId());
        int id = user.getId();
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        User userFromDb = getById(id);

        if (user.getEmail() == null) {
            user.setEmail(userFromDb.getEmail());
        }
        if (user.getName() == null) {
            user.setName(userFromDb.getName());
        }
        users.remove(id);
        if (isDuplicateEmail(user.getEmail())) {
            users.put(userFromDb.getId(), userFromDb);
            throw new DuplicateEmailException("Email already exists");
        }

        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User getById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("No User with such id");
        }

        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delUserById(int id) {
        users.remove(id);
    }

    private boolean isDuplicateEmail(String email) {
        List<String> emailToDb = users.values().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        return emailToDb.contains(email);
    }
}
