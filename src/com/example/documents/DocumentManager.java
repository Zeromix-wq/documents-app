package com.example.documents;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentManager {
    private List<Document> documents = new ArrayList<>();

    public void addDocument(Document doc) {
        documents.add(doc);
    }

    public void removeDocuments(List<Document> toRemove) {
        documents.removeAll(toRemove);
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void saveToFile(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Document doc : documents) {
                writer.println(doc.getTypeName());
                writer.println(doc.getNumber());
                writer.println(doc.getDate().toString());
                writer.println(doc.getUser());
                
                if (doc instanceof Invoice) {
                    Invoice inv = (Invoice) doc;
                    writer.println(inv.getAmount());
                    writer.println(inv.getCurrency());
                    writer.println(inv.getExchangeRate());
                    writer.println(inv.getProduct());
                    writer.println(inv.getQuantity());
                } else if (doc instanceof Payment) {
                    Payment pay = (Payment) doc;
                    writer.println(pay.getAmount());
                    writer.println(pay.getEmployee());
                } else if (doc instanceof PaymentRequest) {
                    PaymentRequest req = (PaymentRequest) doc;
                    writer.println(req.getCounterparty());
                    writer.println(req.getAmount());
                    writer.println(req.getCurrency());
                    writer.println(req.getExchangeRate());
                    writer.println(req.getCommission());
                }
                writer.println("---");
            }
        }
    }

    public void loadFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String type = line;
                String number = reader.readLine();
                String dateStr = reader.readLine();
                String user = reader.readLine();
                LocalDate date = LocalDate.parse(dateStr);

                if (type.equals("Накладная")) {
                    double amount = Double.parseDouble(reader.readLine());
                    String currency = reader.readLine();
                    double rate = Double.parseDouble(reader.readLine());
                    String product = reader.readLine();
                    double quantity = Double.parseDouble(reader.readLine());
                    documents.add(new Invoice(number, date, user, amount, currency, rate, product, quantity));
                } else if (type.equals("Платёжка")) {
                    double amount = Double.parseDouble(reader.readLine());
                    String employee = reader.readLine();
                    documents.add(new Payment(number, date, user, amount, employee));
                } else if (type.equals("Заявка на оплату")) {
                    String counterparty = reader.readLine();
                    double amount = Double.parseDouble(reader.readLine());
                    String currency = reader.readLine();
                    double rate = Double.parseDouble(reader.readLine());
                    double commission = Double.parseDouble(reader.readLine());
                    documents.add(new PaymentRequest(number, date, user, counterparty, amount, currency, rate, commission));
                }
                
                reader.readLine();
            }
        }
    }
}