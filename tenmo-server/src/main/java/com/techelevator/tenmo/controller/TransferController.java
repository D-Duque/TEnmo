package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController
{
    private final TransferDao transferDao;
    private final AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao)
    {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @PutMapping(value = "")
    public void updateBalances(@RequestBody Transfer transfer)
    {
        // get transfer object data
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();

        // get current account info
        Account fromAccount = accountDao.getAccountById(accountFrom);
        Account toAccount = accountDao.getAccountById(accountTo);

        BigDecimal fromBalance = fromAccount.getBalance();
        BigDecimal toBalance = toAccount.getBalance();

        BigDecimal updatedFromBalance = fromBalance.subtract(amount);
        BigDecimal updatedToBalance = toBalance.add(amount);

        fromAccount.setBalance(updatedFromBalance);
        toAccount.setBalance(updatedToBalance);

        // update balances in database
        accountDao.updateAccount(fromAccount);
        accountDao.updateAccount(toAccount);

        // add transfer to transfer table
        addTransfer(transfer);
    }

    // update transfer table
    public void addTransfer(Transfer transfer) {
         transferDao.addTransfer(transfer);
    }
}
