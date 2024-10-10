package com.jialin.BulletinBoard.service;

import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  Handles the business logic of the application.
 *  It interacts with UserRepository to use defined CRUD operations and incorporates additional business logic such as
 *  password encoding.
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return _userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return _userRepository.findAll();
    }

    public User getUserById(Long id) {
        return _userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + id));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return _userRepository.save(user);
    }

    public void deleteUser(Long id) {
        _userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return _userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
