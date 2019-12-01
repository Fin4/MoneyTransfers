package com.test.moneytransfers.dto;

import com.test.moneytransfers.model.Transfer;

public class TransferDto {

  public Long id;
  public Long senderId;
  public Long receiverId;
  public String amount;
  public String rate;
  public String currency;
  public String timestamp;
  public String notes;

  public static TransferDto from(Transfer transfer) {
    var transferDto = new TransferDto();

    transferDto.id = transfer.getId();
    transferDto.receiverId = transfer.getReceiverAccId();
    transferDto.senderId = transfer.getSenderAccId();
    transferDto.amount = transfer.getAmount().toString();
    transferDto.rate = transfer.getRate().toString();
    transferDto.notes = transfer.getNotes();
    transferDto.currency = transfer.getCurrency().toString();
    transferDto.timestamp = String.valueOf(transfer.getTimestamp().getEpochSecond());

    return transferDto;
  }
}
