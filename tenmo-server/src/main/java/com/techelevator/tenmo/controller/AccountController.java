package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController
{
    private final AccountDao accountDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao)
    {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping("/all")
    public List<Account> getAllAccounts()
    {
        return accountDao.findAll();
    }

    @GetMapping(value = "")
    public Account findCurrentAccount(Principal principal)
    {
        int userId = userDao.findByUsername(principal.getName()).getId();
        Account currentAccount = accountDao.getAccountById(userId);
        return currentAccount;
    }

    @GetMapping(value = "/{id}")
    public Account findAccountById(@PathVariable int id)
    {
        Account account = accountDao.getAccountById(id);
        return account;
    }

    @GetMapping(value = "/balance")
    public BigDecimal accountBalance(Principal principal)
    {
        int userId = userDao.findByUsername(principal.getName()).getId();
        return accountDao.getAccountById(userId).getBalance();
    }
}
