package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String firstName; String lastName;String mail;
    Set<AccountDTO> accounts = new HashSet<>();
    Set<ClientLoanDTO> loans = new HashSet<>();
    Set<CardDTO>cards= new HashSet<>();

    public ClientDTO() {    }

    public ClientDTO(Client client) {
                this.id = client.getId();
                this.firstName = client.getFirstName();
                this.lastName = client.getLastName();
                this.mail = client.getMail();
                this.accounts= client.getAccounts().stream().map(accounts -> new AccountDTO(accounts)).collect(Collectors.toSet());
                this.loans= client.getLoans().stream().map(loans -> new ClientLoanDTO(loans)).collect(Collectors.toSet());
                this.cards= client.getCards().stream().map(cards -> new CardDTO(cards)).collect(Collectors.toSet());
    }

    public Set<CardDTO> getCards() {return cards;}
    public void addCards(Set<CardDTO> cards) {this.cards = cards;}

    public Set<ClientLoanDTO> getLoans(){return loans;}
    public void addLoans(Set<ClientLoanDTO> loans){
        this.loans=loans;
    }

    public Set<AccountDTO> getAccounts(){return accounts;}
    public void addAccounts(Set<AccountDTO> accounts){
        this.accounts=accounts;
    }

    public Long getId() {return id;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getMail() {return mail;}
    public void setMail(String mail) {this.mail = mail;}


}
