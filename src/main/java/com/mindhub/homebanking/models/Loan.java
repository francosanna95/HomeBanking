package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    long id;
    String name;
    int maxAmount;

    @OneToMany(mappedBy = "loan",fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans= new HashSet<>();

    @ElementCollection
    @Column(name="payment")
    private List<Integer> payment = new ArrayList<>();

    public Loan() {}

    public Loan(String name, int maxAmount, List<Integer> payment) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payment = payment;
    }

    @JsonIgnore
    public Set<ClientLoan> getClients(){return clientLoans;}
    public void addClient(ClientLoan clientLoan){
        clientLoan.setLoans(this);
        clientLoans.add(clientLoan);
    }

    public long getId() {return id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public double getMaxAmount() {return maxAmount;}
    public void setMaxAmount(int maxAmount) {this.maxAmount = maxAmount;}

    public List<Integer> getPayment() {return payment;}
    public void setPayment(List<Integer> payment) {this.payment = payment;}
}
