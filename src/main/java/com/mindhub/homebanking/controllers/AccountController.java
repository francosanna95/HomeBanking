package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository repo;

    @Autowired
    private TransactionRepository transactionRepository;

    //@RequestMapping(path = "/api/clients/current/accounts",method = RequestMethod.DELETE)

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object>newAccount(Authentication authentication, @RequestParam
    AccountType accountType){
        String number="VIN-"+getRandomNumberString();
        Client client= repo.findBymail(authentication.getName());
        if(accountRepository.findByNumber(number) != null){
            return new ResponseEntity<>("Number already in use", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().size()>=3){
            return new ResponseEntity<>("You cant have more than 3 accounts", HttpStatus.FORBIDDEN);
        }
        Account acc= new Account (number, LocalDateTime.now(),0,accountType);
        client.addAccount(acc);
        accountRepository.save(acc);
        return new ResponseEntity<>(HttpStatus.CREATED);
    };

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());}
    @GetMapping("/api/client/currents/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);}

    public static String getRandomNumberString(){
        Random random = new Random();
        int aleatorio6Digitos = random.nextInt(999999);
        return String.format("%08d", aleatorio6Digitos);
    }

    @DeleteMapping("/api/clients/current/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable long accountId){
        Account acc= accountRepository.getById(accountId);
        if(acc.getBalance()>0){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Set<Transaction> transactions=acc.getTransactions();
        for (Transaction transaction: transactions){
            if (transactionRepository.existsById(transaction.getId())){
                transactionRepository.delete(transaction);
            }
        }
        accountRepository.delete(acc);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
