package com.mindhub.homebanking.dto;

public class CardTransactionDTO {
    private String number;
    private String cvv;
    private double amount;
    private String description;
    private String mail;

    public CardTransactionDTO() {}

    public CardTransactionDTO(String number, String cvv, double amount, String description, String mail) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.mail= mail;
    }

    public String getMail() {return mail;}
    public void setMail(String mail) {this.mail = mail;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}

    public String getCvv() {return cvv;}
    public void setCvv(String cvv) {this.cvv = cvv;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
