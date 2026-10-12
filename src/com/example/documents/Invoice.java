package com.example.documents;

import java.time.LocalDate;

public class Invoice extends Document {
    private double amount;
    private String currency;
    private double exchangeRate;
    private String product;
    private double quantity;

    public Invoice(String number, LocalDate date, String user, 
                   double amount, String currency, double exchangeRate, 
                   String product, double quantity) {
        super(number, date, user);
        this.amount = amount;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String getTypeName() { return "Накладная"; }

    @Override
    public String getDisplayText() {
        return String.format("Накладная от %s номер %s", formatDate(), number);
    }

    @Override
    public String getDetails() {
        return String.format(
            "Номер: %s\nДата: %s\nПользователь: %s\n" +
            "Сумма: %.2f\nВалюта: %s\nКурс Валюты: %.2f\n" +
            "Товар: %s\nКоличество: %.2f",
            number, formatDate(), user, amount, currency, exchangeRate, product, quantity
        );
    }

    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public double getExchangeRate() { return exchangeRate; }
    public String getProduct() { return product; }
    public double getQuantity() { return quantity; }
}