package com.hanu.leaniverse.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class PaymentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentInfoId;
    private String cardType;
    private String bank;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public PaymentInformation() {
    }

    public int getPaymentInfoId() {
        return paymentInfoId;
    }

    public void setPaymentInfoId(int paymentInfoId) {
        this.paymentInfoId = paymentInfoId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
