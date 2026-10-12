package com.example.documents;

import java.time.LocalDate;

public class PaymentRequest extends Document {
    private String counterparty;
    private double amount;
    private String currency;
    private double exchangeRate;
    private double commission;

    public PaymentRequest(String number, LocalDate date, String user, 
                          String counterparty, double amount, String currency, 
                          double exchangeRate, double commission) {
        super(number, date, user);
        this.counterparty = counterparty;
        this.amount = amount;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.commission = commission;
    }

    @Override
    public String getTypeName() { return "Заявка на оплату"; }

    @Override
    public String getDisplayText() {
        return String.format("Заявка на оплату от %s номер %s", formatDate(), number);
    }

    @Override
    public String getDetails() {
        return String.format(
            "Номер: %s\nДата: %s\nПользователь: %s\n" +
            "Контрагент: %s\nСумма: %.2f\nВалюта: %s\n" +
            "Курс Валюты: %.2f\nКомиссия: %.2f",
            number, formatDate(), user, counterparty, amount, currency, exchangeRate, commission
        );
    }

    public String getCounterparty() { return counterparty; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public double getExchangeRate() { return exchangeRate; }
    public double getCommission() { return commission; }
}