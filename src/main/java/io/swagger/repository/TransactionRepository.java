package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {

    @Query("SELECT t FROM Transaction t WHERE t.accountFrom =:iban OR t.accountTo =:iban")
    List<Transaction> findByIban(@Param("iban") String iban);

}
