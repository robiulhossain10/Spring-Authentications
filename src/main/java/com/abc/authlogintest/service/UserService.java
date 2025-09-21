package com.abc.authlogintest.service;

import com.abc.authlogintest.dtos.UserDTO;
import com.abc.authlogintest.entity.User;
import com.abc.authlogintest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<UserDTO> login(UserDTO userDTO) {
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());

        if (user.isPresent() && user.get().getPassword().equals(userDTO.getPassword())) {
            return Optional.of(toDTO(user.get()));
        }
        return Optional.empty();
    }

    @Transactional
    public UserDTO register(UserDTO userDTO){
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        User user = toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    // ✅ নতুন update মেথড
    @Transactional
    public Optional<UserDTO> update(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDTO.getName());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setPassword(userDTO.getPassword());
                    User updatedUser = userRepository.save(existingUser);
                    return toDTO(updatedUser);
                });
    }

    public UserDTO toDTO(User user){
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public User toEntity(UserDTO userDTO){
        if (userDTO == null) return null;

        User user = new User();
        user.setId(userDTO.getId()); // ✅ id সেট করা হলো যাতে update কাজ করে
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
