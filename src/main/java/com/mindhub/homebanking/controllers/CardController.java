package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dto.CardTransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class CardController {
    @Autowired
    private CardRepository cardsRepo;
    @Autowired
    private ClientRepository clRepo;
    @Autowired
    private AccountRepository accRepo;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        Client client= clRepo.findBymail(authentication.getName());
        int counter=0;
        if(client.getCards().size()>=6){
            return new ResponseEntity<>("you can't have more cards",HttpStatus.FORBIDDEN);
        }
        Set<Card>cards=client.getCards();
            for (Card card: cards){
                if(card.getType().equals(cardType)){
               counter++;
            }
        }
        if(counter >=3){
         return new ResponseEntity<>("You can't have more than 3 cards of each type", HttpStatus.FORBIDDEN);
        }
        Card card= new Card(client.getFirstName()+" "+client.getLastName(),
                getRandomNumberStringLength(4)+"-"+getRandomNumberStringLength(4)+"-"+getRandomNumberStringLength(4)+"-"+getRandomNumberStringLength(4),
                getRandomCVVStringLength(3), LocalDateTime.now(),
                LocalDateTime.now().plusYears(5),cardType,cardColor);
        client.addCard(card);
        cardsRepo.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public static String getRandomNumberStringLength(int length){
        Random random = new Random();
        int aleatorio4Digitos = random.nextInt(9999);
        return String.format("%0"+length+"d", aleatorio4Digitos);
    }

    public static String getRandomCVVStringLength(int length){
        Random random = new Random();
        int aleatorio3Digitos = random.nextInt(999);
        return String.format("%0"+length+"d", aleatorio3Digitos);
    }

    @DeleteMapping("/api/clients/current/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable long cardId){
        Card card = cardsRepo.findById(cardId).get();
        cardsRepo.delete(card);
        return new ResponseEntity<>(HttpStatus.GONE);
    }
    @Transactional
    @PostMapping("/api/clients/current/cardTransaction")
    public ResponseEntity<String> cardPayment(@RequestBody CardTransactionDTO cardTransactionDTO) {
        Card card=cardsRepo.findByNumber(cardTransactionDTO.getNumber());
        if (card.getCvv()!=cardTransactionDTO.getCvv()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Client client=clRepo.findBymail(cardTransactionDTO.getMail());

        if (card.getThruDate().toLocalDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        int counter=0;
        for (int i=0;i<client.getAccounts().size();i++){
            if (client.getAccounts().stream().collect(Collectors.toList()).get(i).getBalance()>=cardTransactionDTO.getAmount()){
                counter++;
                if (counter==1) {
                    Account account = client.getAccounts().stream().collect(Collectors.toList()).get(i);
                    account.setBalance(account.getBalance()-cardTransactionDTO.getAmount());
                    Transaction transaction=new Transaction(TransactionType.DEBIT,LocalDateTime.now(),cardTransactionDTO.getAmount()*-1,cardTransactionDTO.getDescription());
                    account.addTransaction(transaction);
                }
            }
        }
        if (counter<1){ return new ResponseEntity<>(HttpStatus.NO_CONTENT);}

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


