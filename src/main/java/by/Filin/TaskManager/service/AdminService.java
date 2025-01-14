package by.Filin.TaskManager.service;

import by.Filin.TaskManager.DTO.user.UserDTO;
import by.Filin.TaskManager.entity.Role;
import by.Filin.TaskManager.entity.User;
import by.Filin.TaskManager.mapper.UserMapper;
import by.Filin.TaskManager.repository.RoleRepository;
import by.Filin.TaskManager.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public void setAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Role role = roleRepository.findByName("ADMIN").orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        user.setRole(role);
        userRepository.save(user);
    }

    public List<UserDTO> getAllusers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }
}
