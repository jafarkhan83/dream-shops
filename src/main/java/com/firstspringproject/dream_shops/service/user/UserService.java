package com.firstspringproject.dream_shops.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.firstspringproject.dream_shops.dto.UserDto;
import com.firstspringproject.dream_shops.exceptions.UserAlreadyExistsException;
import com.firstspringproject.dream_shops.exceptions.UserNotExistsException;
import com.firstspringproject.dream_shops.model.Role;
import com.firstspringproject.dream_shops.model.User;
import com.firstspringproject.dream_shops.repository.RoleRepository;
import com.firstspringproject.dream_shops.repository.UserRepository;
import com.firstspringproject.dream_shops.request.CreateUserRequest;
import com.firstspringproject.dream_shops.request.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotExistsException("No user exists by this id!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
            .filter(user -> !userRepository.existsByEmail(user.getEmail()))
            .map(req -> {
                User user = new User();
                user.setEmail(req.getEmail());
                user.setPassword(passwordEncoder.encode(req.getPassword()));
                user.setFirstName(req.getFirstName());
                user.setLastName(req.getLastName());
                Role role = roleRepository.findByName("CUSTOMER");
                user.getRoles().add(role);
                return userRepository.save(user);
            }).orElseThrow(() -> new UserAlreadyExistsException("This email address is occupied!"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotExistsException("No user exists by this id!"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
            .ifPresentOrElse(userRepository::delete, () -> {
                throw new UserNotExistsException("No user exists by this id!");
            });
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotExistsException("No authenticated user found!");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotExistsException("Authenticated user not found in database!");
        }
        return user;
    }
    
}
