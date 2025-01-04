package model.base;

import java.time.LocalDateTime;

public class Receipt {
    private String receiptId;
    private String recordId;
    private double amount;
    private LocalDateTime createdAt;

    public Receipt() {
    }

    public Receipt(String receiptId, String recordId, double amount) {
        this.receiptId = receiptId;
        this.recordId = recordId;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public Receipt(String receiptId, String recordId, double amount, LocalDateTime createdAt) {
        this.receiptId = receiptId;
        this.recordId = recordId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

