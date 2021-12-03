package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.controllers.AccountController.getRandomNumberString;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RestController
public class ClientController {

    @Autowired
    private ClientRepository repo;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/api/clients")
    public ResponseEntity<Object>register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String mail, @RequestParam String password){
        if(firstName.isEmpty()||lastName.isEmpty() ||mail.isEmpty() ||password.isEmpty())
        {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (repo.findBymail(mail) !=null){
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        String number= "VIN-"+getRandomNumberString();
        Account acc= new Account (number, LocalDateTime.now(),0, AccountType.CURRENT);
        if(accountRepository.findByNumber(number) != null){
            String newNumber;
            do {
                newNumber="VIN-"+getRandomNumberString();
            }while (accountRepository.findByNumber(newNumber) !=null);
            acc.setNumber(newNumber);
        }
        Client client= new Client(firstName,lastName,mail,passwordEncoder.encode(password));
        repo.save(client);
        client.addAccount(acc);
        accountRepository.save(acc);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return repo.findAll().stream().map(ClientDTO::new).collect(toList());
    }
   @GetMapping("/api/clients/{id}")
    public ClientDTO  getClient(@PathVariable Long id){
        return repo.findById(id).map(ClientDTO::new).orElse(null);
   }

    @GetMapping("/api/clients/current")
    public ClientDTO getClientAuth(Authentication authentication){
       Client client= repo.findBymail(authentication.getName());
       ClientDTO clientDTO= new ClientDTO(client);
       return clientDTO;
    }


}
