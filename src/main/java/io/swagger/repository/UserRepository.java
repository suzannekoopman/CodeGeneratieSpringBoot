package io.swagger.repository;

import io.swagger.model.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email =:email AND u.password = :password")
    User findUserByUserCredentials(@Param("email") String email,@Param("password") String password);

    @Query("SELECT u FROM User u WHERE u.email =:email")
    User findUserByEmail(@Param("email") String email);

    @Query("Select u FROM User u WHERE LOWER(u.lastName) =:lastName")
    List<User> findUsersByLastName(@Param("lastName") String lastName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.firstName = :newFirstName, u.lastName = :newLastName, u.password = :newPassword, u.email = :newEmail WHERE u.userId = :userId")
    void updateUser(@Param("newFirstName") String newFirstName, @Param("newLastName") String newLastName, @Param("newEmail") String newEmail, @Param("newPassword") String newPassword, @Param("userId") int userId);

}