package model.base;

import util.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId='" + receiptId + '\'' +
                ", recordId='" + recordId + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }

    public String[] toStrings(){
        return new String[]{receiptId, recordId, String.valueOf(amount), Utils.localDateTimeToStringWithTime(createdAt)};
    }
}