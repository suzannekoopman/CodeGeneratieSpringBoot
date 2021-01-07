package io.swagger.repository;

import io.swagger.model.RegisterRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RegisterRequestRepository extends CrudRepository<RegisterRequest, Integer> {

    @Query("SELECT r FROM RegisterRequest r WHERE r.email =:email")
    RegisterRequest findUserByEmail(@Param("email") String email);
}
