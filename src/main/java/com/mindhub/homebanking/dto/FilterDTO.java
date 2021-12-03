package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FilterDTO {
    private String accountNumber="";
    private LocalDate sinceDate;
    private LocalDate thruDate;

    public FilterDTO() {}

    public FilterDTO(String accountNumber, LocalDate sinceDate, LocalDate thruDate) {
        this.accountNumber = accountNumber;
        this.sinceDate = sinceDate;
        this.thruDate = thruDate;
    }

    public String getAccountNumber() {return accountNumber;}
    public void setAccountNumber(String accountNumber) {this.accountNumber = accountNumber;}

    public LocalDate getSinceDate() {return sinceDate;}
    public void setSinceDate(LocalDate sinceDate) {this.sinceDate = sinceDate;}

    public LocalDate getThruDate() {return thruDate;}
    public void setThruDate(LocalDate thruDate) {this.thruDate = thruDate;}
}
