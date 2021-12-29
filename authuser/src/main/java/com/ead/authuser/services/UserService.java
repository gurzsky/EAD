package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findALL();
    Optional<UserModel> findById(UUID userId);
    void delete(UserModel userModel);
    void save(UserModel userModel);
    boolean existsByUserName(String userName);
    boolean existsByUserEmail(String email);
    Page<UserModel> findALL(Pageable pageable);
}
