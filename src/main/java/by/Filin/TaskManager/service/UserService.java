package by.Filin.TaskManager.service;

import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.DTO.user.UserRequestDTO;
import by.Filin.TaskManager.DTO.user.UserUpdateDTO;
import by.Filin.TaskManager.entity.Role;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.mapper.UserMapper;
import by.Filin.TaskManager.repository.RoleRepository;
import by.Filin.TaskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // Объект для хэширования и проверки паролей

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                {
                    logger.info("User whit id " + id + " was not found");
                    return new EntityNotFoundException("User not found");
                });
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO createUser(@Valid UserRequestDTO userRequestDTO) {

        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            logger.warning("Registration failed: Username already exists - " + userRequestDTO.getUsername());
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            logger.warning("Registration failed: Email already exists - " + userRequestDTO.getEmail());
            throw new RuntimeException("Email already exists");
        }

        Role roleUser = roleRepository.findByName("USER").orElseThrow(
                () ->
                {
                    log.error("Role not found");
                    return new RuntimeException("Role not found");
                }
        );

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(roleUser);

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, @Valid UserUpdateDTO userUpdateDTO) {
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

//        Устанавливаем время, при котором пользователь был обновлён
        user.setUpdatedAt(OffsetDateTime.parse(OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MICROS).format(DateTimeFormatter.ISO_INSTANT)));
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }

}