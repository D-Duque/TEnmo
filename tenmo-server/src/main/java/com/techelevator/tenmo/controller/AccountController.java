package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

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

    @GetMapping(value = "/balance")
    public BigDecimal accountBalance(Principal principal)
    {
        int userId = userDao.findByUsername(principal.getName()).getId();

        return accountDao.getAccountById(userId).getBalance();
    }

}
