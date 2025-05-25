package com.scheduler.scheduler.LV3.repository;

import com.scheduler.scheduler.LV3.entity.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);


    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User updateName(Long userId, String newName, User user);
}
