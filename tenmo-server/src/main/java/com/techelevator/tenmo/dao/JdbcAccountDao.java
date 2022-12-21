package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);

        while (result.next())
        {
            Account account =  mapRowToAccount(result);
            accounts.add(account);
        }
        return accounts;
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

    @Override
    public Account getAccountByAccountId(int accountId)
    {
        String sql = "SELECT * FROM account WHERE account_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);

        if (result.next())
        {
            return mapRowToAccount(result);
        }
        else
        {
            return null;
        }
    }

    //TODO: update account balances
    @Override
    public void updateAccount(Account updatedAccount)
    {
        int accountId = updatedAccount.getAccountId();
        int userId = updatedAccount.getUserId();
        BigDecimal balance = updatedAccount.getBalance();

        String sqlUpdate = "UPDATE account SET account_id = ? "
                    + " , user_id = ? "
                    + " , balance = ? "
                    + " WHERE user_id = ?;";

        jdbcTemplate.update(sqlUpdate, accountId, userId, balance, userId);
    }

    private Account mapRowToAccount(SqlRowSet rs)
    {

        var accountId = rs.getInt("account_id");
        var userId = rs.getInt("user_id");
        var balance = rs.getBigDecimal("balance");
        Account account = new Account(){{
            setAccountId(accountId);
            setUserId(userId);
            setBalance(balance);
        }};

        return account;
    }
}
