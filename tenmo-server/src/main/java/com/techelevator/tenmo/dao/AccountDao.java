package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao
{
    List<Account> findAll();

    Account getAccountById(int userId);

    void updateAccount(Account updatedAccount);

}
