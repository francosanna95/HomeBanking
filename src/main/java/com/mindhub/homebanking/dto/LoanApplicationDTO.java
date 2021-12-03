package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {
    private Long id;
    private double amount;
    private int payments;
    private String number;

    public LoanApplicationDTO() {}

    public LoanApplicationDTO(Long id, double amount, int payments, String number) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.number = number;
    }

    public Long getId() {return id;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public int getPayments() {return payments;}
    public void setPayments(int payments) {this.payments = payments;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
}
