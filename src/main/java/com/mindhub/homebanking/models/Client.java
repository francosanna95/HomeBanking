package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName; String lastName;String mail;String password;

    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    Set<ClientLoan> loans= new HashSet<>();

    @OneToMany(mappedBy = "cardOwner",fetch = FetchType.EAGER)
    Set<Card> cards= new HashSet<>();

    public Client() { }

    public Client(String firstName, String lastName, String mail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
    }

    public Set<Card> getCards() {return cards;}
    public void addCard(Card card) {
        card.setCardOwner(this);
        cards.add(card);
    }

    public Set<ClientLoan> getLoans(){return loans;}
    public void addLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        loans.add(clientLoan);
    }

    public Set<Account> getAccounts(){return accounts;}
    public void addAccount(Account account){
        account.setOwner(this);
        accounts.add(account);
    }

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName;}

    public String getMail() { return mail;}
    public void setMail(String eMail) {this.mail = eMail;}

    public Long getId() {return id;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", accounts=" + accounts +
                ", clientLoans=" + loans +
                '}';
    }
}
