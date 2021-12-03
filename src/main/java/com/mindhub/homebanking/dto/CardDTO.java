package com.mindhub.homebanking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {
    private Long id;
    private String cardHolder;
    private String number;
    private String cvv;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/YY")
    private LocalDateTime fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/YY")
    private LocalDateTime thruDate;
    private CardType type;
    private CardColor color;

    public CardDTO() { }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder= card.getCardHolder();
        this.number= card.getNumber();
        this.cvv= card.getCvv();
        this.fromDate= card.getFromDate();
        this.thruDate= card.getThruDate();
        this.type= card.getType();
        this.color= card.getColor();
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
}
