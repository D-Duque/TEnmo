package com.techelevator.tenmo.models;

import java.math.BigDecimal;


public class Transfer
{
    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;
    private String username;
    private String fromUsername;
    private String toUserName;
    private String transferType;
    private String transferStatus;

    public Transfer()
    {
    }

    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount)
    {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public int getTransferId()
    {
        return transferId;
    }

    public void setTransferId(int transferId)
    {
        this.transferId = transferId;
    }

    public int getTransferTypeId()
    {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId)
    {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId()
    {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId)
    {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom()
    {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom)
    {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo()
    {
        return accountTo;
    }

    public void setAccountTo(int accountTo)
    {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getFromUsername()
    {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername)
    {
        this.fromUsername = fromUsername;
    }

    public String getToUserName()
    {
        return toUserName;
    }

    public void setToUserName(String toUserName)
    {
        this.toUserName = toUserName;
    }

    public String getTransferType()
    {
        return transferType;
    }

    public void setTransferType(String transferType)
    {
        this.transferType = transferType;
    }

    public String getTransferStatus()
    {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus)
    {
        this.transferStatus = transferStatus;
    }
}
