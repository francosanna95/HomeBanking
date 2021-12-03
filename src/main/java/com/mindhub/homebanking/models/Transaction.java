package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private TransactionType type;
    private LocalDateTime creationDate;
    private double balance=0;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="account_id")
    private Account acc;

    public Transaction() {    }

    public Transaction(TransactionType type, LocalDateTime creationDate, double balance, String description) {
        this.type = type;
        this.creationDate = creationDate;
        this.balance = balance;
        this.description = description;
    }

    public Long getId() {return id;}

    public TransactionType getType() {return type;}
    public void setType(TransactionType type) {this.type = type;}

    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    @JsonIgnore
    public Account getAcc() {return acc;}
    public void setAcc(Account acc) {this.acc = acc;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public void debitOrCredit(){
        switch (this.type){
            case DEBIT:
                if (this.balance>=0){
                    this.balance= this.balance*-1;
                }
                System.out.println("Perdiste plata");
                break;
            case CREDIT:
                if (this.balance<=0){
                    this.balance= this.balance*-1;
                }
                System.out.println("Ganaste plata");
                break;
            default:
                System.out.println("solo aceptamos CREDITO o DEBITO como tipo de transaccion");
        }
    }
    public void debit(TransactionType type, double amount){
        if (type.equals(TransactionType.DEBIT)){
            amount= -amount;
        }

    }
}
