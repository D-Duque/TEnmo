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
    final int ST_PENDING = 1;
    final int ST_APPROVED = 2;
    final int ST_REJECT = 3;
    final int TP_REQUEST = 1;
    final int TP_SEND = 2;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao)
    {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @PutMapping(value = "")
    public void createSend(@RequestBody Transfer transfer)
    {
        // add transfer to transfer table
        transfer = addTransfer(transfer, ST_APPROVED, TP_SEND);

        updateBalances(transfer);
    }

    public void updateBalances(Transfer transfer)
    {
        // get current account IDs
        Account fromAccount = accountDao.getAccountByAccountId(transfer.getAccountFrom());
        Account toAccount = accountDao.getAccountByAccountId(transfer.getAccountTo());
        BigDecimal oldFromBalance = fromAccount.getBalance();
        BigDecimal oldToBalance = toAccount.getBalance();
        BigDecimal updatedAmount = transfer.getAmount();

        // calculate new balances
        BigDecimal newFromBalance = oldFromBalance.subtract(updatedAmount);
        BigDecimal newToBalance = oldToBalance.add(updatedAmount);
        fromAccount.setBalance(newFromBalance);
        toAccount.setBalance(newToBalance);

        // update account database
        accountDao.updateAccount(fromAccount);
        accountDao.updateAccount(toAccount);
    }

    // update transfer table
    public Transfer addTransfer(Transfer transfer, int typeId, int statusId) {
        transfer.setTransferTypeId(typeId);
        transfer.setTransferStatusId(statusId);
        Integer transferId = transferDao.addTransfer(transfer);
        transfer = transferDao.getTransferById(transferId);
        return transfer;
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
            Integer fromUserId = accountDao.getAccountByAccountId(fromAccount).getUserId();
            Integer toUserId = accountDao.getAccountByAccountId(toAccount).getUserId();
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
        Integer fromUserId = accountDao.getAccountByAccountId(fromAccount).getUserId();
        Integer toUserId = accountDao.getAccountByAccountId(toAccount).getUserId();
        String fromUsername = userDao.getUserById(fromUserId).getUsername();
        String toUsername = userDao.getUserById(toUserId).getUsername();
        transfer.setFromUsername(fromUsername);
        transfer.setToUserName(toUsername);
        return transfer;
    }

    @PostMapping(value = "/request")
    public void createRequest(@RequestBody Transfer transfer) {
        addTransfer(transfer, ST_PENDING, TP_REQUEST);
    }

    @PutMapping(value = "/request/{transferId}")
    public void updateRequest(@RequestBody Transfer transfer, @PathVariable int transferId) {

//        Account fromAccount = accountDao.getAccountById(transfer.getAccountFrom());
//        Account toAccount = accountDao.getAccountById(transfer.getAccountTo());
//        transfer.setAccountFrom(fromAccount.getAccountId());
//        transfer.setAccountTo(toAccount.getAccountId());

        if (transfer.getTransferStatusId() == ST_APPROVED) {
            //update balances & DAO
            updateBalances(transfer);

        }
        transferDao.updateTransfer(transfer);
    }

}
