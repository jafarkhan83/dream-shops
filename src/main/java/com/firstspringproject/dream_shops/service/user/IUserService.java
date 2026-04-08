package com.firstspringproject.dream_shops.service.user;

import com.firstspringproject.dream_shops.model.User;
import com.firstspringproject.dream_shops.request.CreateUserRequest;
import com.firstspringproject.dream_shops.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);
}
