package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardHolder;
    private String number;
    private String cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private CardType type;
    private CardColor color;

    @ManyToOne
    @JoinColumn(name ="cardOwner_id")
    private Client cardOwner;

    public Card() {}

    public Card(String cardHolder, String number, String cvv, LocalDateTime fromDate, LocalDateTime thruDate, CardType type, CardColor color) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.type = type;
        this.color = color;
    }

    public Long getId() {return id;}

    public String getCardHolder() {return cardHolder;}
    public void setCardHolder(String cardHolder) {this.cardHolder = cardHolder;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}

    public String getCvv() {return cvv;}
    public void setCvv(String cvv) {this.cvv = cvv;}

    public LocalDateTime getFromDate() {return fromDate;}
    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}

    public LocalDateTime getThruDate() {return thruDate;}
    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}

    public CardType getType() {return type;}
    public void setType(CardType type) {this.type = type;}

    public CardColor getColor() {return color;}
    public void setColor(CardColor color) {this.color = color;}

    public Client getCardOwner() {return cardOwner;}
    public void setCardOwner(Client cardOwner) {this.cardOwner = cardOwner;}
}
