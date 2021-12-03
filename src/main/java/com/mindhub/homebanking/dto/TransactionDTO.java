package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private LocalDate creationDate;
    private double balance=0;
    private String description;

    public TransactionDTO() {}
    public TransactionDTO(Transaction transaction){
        this.id= transaction.getId();
        this.type= transaction.getType();
        this.creationDate= transaction.getCreationDate().toLocalDate();
        this.balance= transaction.getBalance();
        this.description= transaction.getDescription();
    }


    public Long getId() {return id;}

    public TransactionType getType() {return type;}
    public void setType(TransactionType type) {this.type = type;}

    public LocalDate getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDate creationDate) {this.creationDate = creationDate;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", type=" + type +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", description='" + description + '\'' +
                '}';
    }
}
