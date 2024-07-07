package com.jialin.BulletinBoard.service;

import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUsername("user_1");
        user.setPassword("password_1"); // Normally you'd want this encrypted
        user.setEmail("user_1@gmail.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.saveUser(user);

        assertNotNull(createdUser);
        assertEquals("user_1", createdUser.getUsername());
        assertEquals("password_1", createdUser.getPassword()); // password_1 is not encrypted
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user_1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("user_1", foundUser.getUsername());
    }

    @Test
    void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("user_1");
        existingUser.setPassword("password_1");
        existingUser.setEmail("user_1@gmail.com");

        User newUserDetails = new User();
        newUserDetails.setUsername("user_2");
        newUserDetails.setPassword("password_2");
        newUserDetails.setEmail("user_2@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(newUserDetails);

        User user_2 = userService.updateUser(1L, newUserDetails);

        assertNotNull(user_2);
        assertEquals("user_2", user_2.getUsername());
        assertEquals("password_2", user_2.getPassword()); // Note password_1 is also not encrypted
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
}