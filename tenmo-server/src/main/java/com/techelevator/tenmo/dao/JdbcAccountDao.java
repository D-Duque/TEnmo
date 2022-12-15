package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao
{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAll()
    {
        return null;
    }

    @Override
    public Account getAccountById(int userId)
    {
        String sql = "SELECT * FROM account WHERE user_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);

        if (result.next())
        {
            return mapRowToAccount(result);
        }
        else
        {
            return null;
        }
    }

    //TODO: findBalanceByID

    private Account mapRowToAccount(SqlRowSet rs)
    {
//        Account account = new Account();
//        account.setAccountId(rs.getInt("account_id"));
//        account.setUserId(rs.getInt("user_id"));
//        account.setBalance(rs.getBigDecimal("balance"));
//        return account;

        var accountId = rs.getInt("account_id");
        var userId = rs.getInt("user_id");
        var balance = rs.getBigDecimal("balance");
        var account = new Account(){{
            setAccountId(accountId);
            setUserId(userId);
            setBalance(balance);
        }};

        return account;
    }
}
