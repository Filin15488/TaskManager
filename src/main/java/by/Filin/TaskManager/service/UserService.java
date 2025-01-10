package by.Filin.TaskManager.service;

import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.DTO.user.UserRequestDTO;
import by.Filin.TaskManager.DTO.user.UserUpdateDTO;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.mapper.UserMapper;
import by.Filin.TaskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // Объект для хэширования и проверки паролей

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userUpdateDTO.getUsername() != null) {
            user.setUsername(userUpdateDTO.getUsername());
        }
        if (userUpdateDTO.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }

}