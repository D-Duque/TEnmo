package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController
{
    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserDao userDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao)
    {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
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

    // get transfer list
    @GetMapping(value = "/history")
    public List<Transfer> getTransfers(Principal principal) {
        List<Transfer> transfers = new ArrayList<>();
        int userId = userDao.findByUsername(principal.getName()).getId();
        int accountId = accountDao.getAccountById(userId).getAccountId();


        transfers = transferDao.findAll(accountId);
        for (Transfer transfer : transfers)
        {
            Integer fromAccount = transfer.getAccountFrom();
            Integer toAccount = transfer.getAccountTo();
            Integer fromUserId = accountDao.getAccountbyAccountId(fromAccount).getUserId();
            Integer toUserId = accountDao.getAccountbyAccountId(toAccount).getUserId();
            String fromUsername = userDao.getUserById(fromUserId).getUsername();
            String toUsername = userDao.getUserById(toUserId).getUsername();
            transfer.setFromUsername(fromUsername);
            transfer.setToUserName(toUsername);
        }
        return transfers;
    }

    @GetMapping(value = "/{transferId}")
    public Transfer getTransferDetail(@PathVariable int transferId)
    {   
        Transfer transfer = transferDao.getTransferById(transferId);
        Integer fromAccount = transfer.getAccountFrom();
        Integer toAccount = transfer.getAccountTo();
        Integer fromUserId = accountDao.getAccountbyAccountId(fromAccount).getUserId();
        Integer toUserId = accountDao.getAccountbyAccountId(toAccount).getUserId();
        String fromUsername = userDao.getUserById(fromUserId).getUsername();
        String toUsername = userDao.getUserById(toUserId).getUsername();
        transfer.setFromUsername(fromUsername);
        transfer.setToUserName(toUsername);

       return transfer;
    }

}
