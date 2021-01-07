package io.swagger.repository;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import io.swagger.model.User;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest

public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByExistingUserCredentialsShouldReturnNotNull() throws Exception
    {
        User user = userRepository.findUserByUserCredentials("dylanEmail", "5f4dcc3b5aa765d61d8327deb");
        assertNotNull(user);
    }

    @Test
    public void findUserByNotExistingUserCredentialsShouldReturnNull() throws Exception
    {
        User user = userRepository.findUserByUserCredentials("notExisting", "password");
        assertNull(user);
    }

    @Test
    public void findUserByExistingEmailShouldNotReturnNull() throws Exception {
        assertTrue(userRepository.findUserByEmail("dylanEmail") != null);
    }

    @Test
    public void findUserByNonExistingEmailShouldReturnNull() throws Exception {
        assertNull(userRepository.findUserByEmail("nonexisting@gmai.com"));
    }

    @Test
    public void findUserByExistingLastNameShouldNotReturnNull() throws Exception {
        assertNotNull(userRepository.findUsersByLastName("user"));
    }

    @Test
    public void findUsersByNonExistingLastNameShouldReturnEmptyList() throws Exception {
        List<User> users = (userRepository.findUsersByLastName("nonExistingLastName"));
        assertTrue(users.size() == 0);
    }


    @Test
    public void updateUserByExistingIdShouldSuccessfullyUpdateUser() throws Exception {
        userRepository.updateUser("John", "Doe", "suus@gmail.com", "password", 100006);
        assertNotNull(userRepository.findUserByUserCredentials("suus@gmail.com", "password"));
    }

    @Test
    public void updateUserByNonExistingIdShouldFailToUpdateUser() throws Exception {
        userRepository.updateUser("John", "Doe", "otheremail@gmail.com", "newPassword1!", 00000);
        assertTrue(userRepository.findUserByUserCredentials("otheremail@gmail.com", "newPassword1!") == null);
    }

}