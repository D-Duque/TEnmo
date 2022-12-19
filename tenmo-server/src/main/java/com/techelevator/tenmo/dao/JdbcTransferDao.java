package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao
{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> findAll(int userId)
    {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_from = (SELECT account_id FROM account WHERE user_id = ?)    \n" +
                "OR account_to = (SELECT account_id FROM account WHERE user_id = ?);";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (result.next())
        {
            Transfer transfer =  mapRowToTransfer(result);
            transfers.add(transfer);
        }
        return transfers;
    }


    @Override
    public Transfer getTransferById(int transferId)
    {
        return null;
    }

    @Override
    public boolean addTransfer(Transfer transfer)
    {
        Integer transferId;
        int userFrom = transfer.getAccountFrom();
        int userTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES ((SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = 'Send'),\n" +
                "        (SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = 'Approved'),\n" +
                "        (SELECT account_id FROM account WHERE user_id = ?), " +
                "(SELECT account_id FROM account WHERE user_id = ?), " +
                "?) RETURNING transfer_id;";

        transferId = jdbcTemplate.queryForObject(sql, Integer.class, userFrom, userTo, amount);

        return transferId != null;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs)
    {

        var transferTypeId = rs.getInt("transfer_type_id");
        var transferStatusId = rs.getInt("transfer_status_id");
        var accountFrom = rs.getInt("account_from");
        var accountTo = rs.getInt("account_to");
        var amount = rs.getBigDecimal("amount");
        var transfer = new Transfer(){{
            setTransferTypeId(transferTypeId);
            setTransferStatusId(transferStatusId);
            setAccountFrom(accountFrom);
            setAccountTo(accountTo);
            setAmount(amount);
        }};

        return transfer;
    }
}
