package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ExportPDF;
import com.mindhub.homebanking.dto.FilterDTO;
import com.mindhub.homebanking.dto.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RestController
public class TransactionController {
     @Autowired
    TransactionRepository transactionRepository;

     @Autowired
     ClientRepository clientRepository;

     @Autowired
    AccountRepository accountRepository;

     @Transactional
    @PostMapping("/api/clients/current/transaction")
    public ResponseEntity<Object> newTransaction(
            @RequestParam int amount, @RequestParam String description,
            @RequestParam String originAccount, @RequestParam String destinyAccount, Authentication authentication){
         if(amount<1){return new ResponseEntity<>("invalid amount, remember you must send positive numbers",HttpStatus.FORBIDDEN);}
        if(originAccount.equals(destinyAccount)){
            return new ResponseEntity<>("invalid destiny, please select other destiny account", HttpStatus.FORBIDDEN);
        }
        int counter=0;
        Client client= clientRepository.findBymail(authentication.getName());
        Set<Account>accounts=client.getAccounts();
        for(Account account:accounts){
            if(account.getNumber().equals(originAccount)){
                counter++;
            }
        }
        if(counter==0){
            return new ResponseEntity<>("Invalid Account Number",HttpStatus.FORBIDDEN);
        }
        Account account = accountRepository.findByNumber(originAccount);

        if(accountRepository.findByNumber(destinyAccount)== null){
            return new ResponseEntity<>("invalid destiny account",HttpStatus.FORBIDDEN);
        }
        Account account2= accountRepository.findByNumber(destinyAccount);

        if(account.getBalance()<amount){
            return new ResponseEntity<>("Not enough founts",HttpStatus.FORBIDDEN);
        }else{
            account.setBalance(account.getBalance()-amount);
            account2.setBalance(account2.getBalance()+amount);
        }
        double debitAmount= amount-(amount*2);
        Transaction transaction= new Transaction(DEBIT, LocalDateTime.now(),debitAmount,description+" to "+destinyAccount);
        transaction.debit(transaction.getType(),amount);
        account.addTransaction(transaction);
        Transaction transaction2= new Transaction(CREDIT,LocalDateTime.now(),amount,description+" from "+originAccount);
        account2.addTransaction(transaction2);
        transactionRepository.save(transaction);
        transactionRepository.save(transaction2);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/clients/current/transactionspdf")
    public void exportToPDF(@RequestBody FilterDTO filterDTO,HttpServletResponse response){
        Account account= accountRepository.findByNumber(filterDTO.getAccountNumber());
        response.setContentType("application/pdf");
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String fileName= dateFormat.format(new Date());
        String headerKey="content-disposition";
        String headerValue="attachment; filename=voucher "+fileName;
        response.setHeader(headerKey,headerValue);

        LocalDate start=filterDTO.getSinceDate();
        LocalDate end=filterDTO.getThruDate().plusDays(1);
        Stream<LocalDate> dates= start.datesUntil(end);
        List <LocalDate> datesList=dates.collect(Collectors.toList());

        List<TransactionDTO> transactionDTOS=account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList=transactionDTOS.stream().filter(a->datesList.contains(a.getCreationDate())).collect(Collectors.toList());
    //    for (TransactionDTO transaction: transactionDTOS) {
    //        for (LocalDate date: datesList){
    //            if (date.isEqual(transaction.getCreationDate())){
    //                transactionDTOList.add(transaction);
    //            }
    //        }
    //    }
        transactionDTOList.sort(Comparator.comparing(TransactionDTO::getCreationDate).reversed());

        ExportPDF exportPDF=new ExportPDF(transactionDTOList);
        exportPDF.export(response);
    }
}
//    @PostMapping("/api/clients/current/transactionspdf")
//    public void exportToPDF(HttpServletResponse response, @RequestParam  String accNumber, @RequestParam S fromDate, @RequestParam String toDate){
//        FilterDTO filterDTO=new FilterDTO(accNumber,)


//    @PostMapping("/api/clients/current/transactionspdf")
//    public void exportToPDF(HttpServletResponse response, @RequestBody FilterDTO filterDTO){
//        Account account= accountRepository.findByNumber(filterDTO.getAccountNumber());
//        response.setContentType("application/pdf");
//        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        String fileName= dateFormat.format(new Date());
//        String headerKey="content-disposition";
//        String headerValue="attachment; filename=voucher "+fileName;
//        response.setHeader(headerKey,headerValue);

//        LocalDate start=filterDTO.getSinceDate();
//        LocalDate end=filterDTO.getThruDate().plusDays(1);
//        Stream<LocalDate> dates= start.datesUntil(end);
//        List <LocalDate> datesList=dates.collect(Collectors.toList());

//        List<TransactionDTO> transactionDTOS=account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
//        List<TransactionDTO> transactionDTOList=transactionDTOS.stream().filter(a->datesList.contains(a.getCreationDate())).collect(Collectors.toList());
        //    for (TransactionDTO transaction: transactionDTOS) {
        //        for (LocalDate date: datesList){
        //            if (date.isEqual(transaction.getCreationDate())){
        //                transactionDTOList.add(transaction);
        //            }
        //        }
        //    }
//        transactionDTOList.sort(Comparator.comparing(TransactionDTO::getCreationDate).reversed());

//        ExportPDF exportPDF=new ExportPDF(transactionDTOList);
//        exportPDF.export(response);
//    }
//}

