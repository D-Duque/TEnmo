package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    public List<Transfer> findAll()
    {
        return null;
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
}
