package com.jialin.BulletinBoard.service;

import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *  Handles the business logic of the application.
 *  It interacts with UserRepository to use defined CRUD operations and incorporates additional business logic such as
 *  password encoding.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository _userRepository;

    public User saveUser(User user) {
        user.setPassword(user.getPassword());
        return _userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return _userRepository.findAll();
    }

    public User getUserById(Long id) {
        return _userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public User updateUser(Long id, User userDetails) {
        User user = _userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return _userRepository.save(user);
    }

    public void deleteUser(Long id) {
        _userRepository.deleteById(id);
    }
}
