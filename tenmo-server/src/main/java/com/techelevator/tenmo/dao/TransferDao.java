package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao
{
    List<Transfer> findAll(int userId);

    Transfer getTransferById(int transferId);

    boolean addTransfer(Transfer transfer);
}
