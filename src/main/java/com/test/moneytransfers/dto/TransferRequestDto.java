package com.test.moneytransfers.dto;

public class TransferRequestDto {

  private Long senderId;

  private Long receiverId;

  private String amount;

  private String notes;

  public TransferRequestDto() {
  }

  public TransferRequestDto(Long senderId, Long receiverId, String amount, String notes) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.amount = amount;
    this.notes = notes;
  }

  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public Long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
