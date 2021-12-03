package com.mindhub.homebanking.repositories;


import java.util.List;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository <Account,Long>{
    Account findByNumber(String number);
    Account getById(long accountId);

}
