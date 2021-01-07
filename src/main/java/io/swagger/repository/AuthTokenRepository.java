package io.swagger.repository;

import io.swagger.model.AuthToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface AuthTokenRepository extends CrudRepository<AuthToken,String> {


    @Query("SELECT t FROM AuthToken t WHERE t.userId =:userId")
    AuthToken findAuthTokenByUser(@Param("userId") int userId);


    @Modifying
    @Transactional
    @Query("DELETE FROM AuthToken t WHERE t.tokenExpires <= :dateTime")
    void deleteAuthTokenByDate(@Param("dateTime") LocalDateTime dateTime);
}
