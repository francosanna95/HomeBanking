package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/api/loans")
    public List<LoanDTO>getLoan(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<String> loanRequest(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        if (loanApplicationDTO.getAmount()<=0){
            return new ResponseEntity<>("The loan ammount should be higher than 0", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments()<=0){
            return new ResponseEntity<>("The number of payments must be more than 0", HttpStatus.FORBIDDEN);
        }
        if (loanRepository.getById(loanApplicationDTO.getId()) ==null) {
            return new ResponseEntity<>("Invalid loan ID", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount()>loanRepository.getById(loanApplicationDTO.getId()).getMaxAmount()){
            return new ResponseEntity<>("The amount required exceeds the limit", HttpStatus.FORBIDDEN);
        }
        int counter=0;
        for (int i=0;i<loanRepository.getById(loanApplicationDTO.getId()).getPayment().size();i++){
            if (loanRepository.getById(loanApplicationDTO.getId()).getPayment().get(i)==loanApplicationDTO.getPayments()){
                counter++;
            }
        }
        if (counter==0){
            return new ResponseEntity<>("Invalid payment option",HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(loanApplicationDTO.getNumber())==null){
            return new ResponseEntity<>("The destiny account doesn't exist",HttpStatus.FORBIDDEN);
        }
            int accountfinder=0;
        for (int i=0;i<clientRepo.findBymail(authentication.getName()).getAccounts().size();i++){

            if (clientRepo.findBymail(authentication.getName()).getAccounts().stream().collect(Collectors.toList()).get(i).getNumber()
                    .equals(loanApplicationDTO.getNumber())){
                accountfinder=accountfinder+1;
            }

        }
        if (accountfinder==0){
            return new ResponseEntity<>("invalid destiny account",HttpStatus.FORBIDDEN);
        }
        double loanAmount=(loanApplicationDTO.getAmount()*20/100)+loanApplicationDTO.getAmount();
        ClientLoan clientLoan=new ClientLoan(loanAmount,loanApplicationDTO.getPayments(),clientRepo.findBymail(authentication.getName()),loanRepository.getById(loanApplicationDTO.getId()));
        clientLoanRepository.save(clientLoan);

        Transaction transaction=new Transaction(CREDIT, LocalDateTime.now(),loanApplicationDTO.getAmount(),loanRepository.getById(loanApplicationDTO.getId()).getName()+ " loan approved");
        transactionRepository.save(transaction);

        Account account = accountRepository.findByNumber(loanApplicationDTO.getNumber());
        account.setBalance(account.getBalance()+loanApplicationDTO.getAmount());
        account.addTransaction(transaction);

        return new ResponseEntity<>("Loan approved",HttpStatus.CREATED);
    }
    
}
