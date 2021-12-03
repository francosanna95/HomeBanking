package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number="";
    private LocalDateTime creationDate;
    private double balance=0;
    private AccountType accountType;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name ="owner_id")
    private Client owner;

    @OneToMany(mappedBy = "acc",fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    public Account() {   };

    public Account(String number, LocalDateTime creationDate, double balance, AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.accountType=accountType;
    };

    public AccountType getAccountType() {return accountType;}
    public void setAccountType(AccountType accountType) {this.accountType = accountType;}

    public Set<Transaction> getTransactions(){return transactions;}
    public void addTransaction(Transaction transaction){transaction.setAcc(this);
        transactions.add(transaction);}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    @JsonIgnore
    public Client getOwner() {return owner;}
    public void setOwner(Client owner) {this.owner = owner;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}




}
