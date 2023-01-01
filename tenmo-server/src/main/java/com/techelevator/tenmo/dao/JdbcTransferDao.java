package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    public List<Transfer> findAll(int accountId)
    {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT DISTINCT ON (transfer_id) transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount, a.account_id, a.user_id, username\n" +
                "FROM transfer as t\n" +
                "JOIN account as a\n" +
                "ON t.account_from = a.account_id \n" +
                "OR t.account_to = a.account_id\n" +
                "JOIN tenmo_user as tu\n" +
                "ON a.user_id = tu.user_id\n" +
                "WHERE t.account_from = ? OR t.account_to = ?; ";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
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
        String sql = "SELECT transfer_id, t.transfer_type_id, transfer_type_desc, t.transfer_status_id, transfer_status_desc, account_from, account_to, amount, a.account_id, a.user_id, username\n" +
                "FROM transfer as t\n" +
                "JOIN account as a\n" +
                "ON t.account_from = a.account_id \n" +
                "OR t.account_to = a.account_id\n" +
                "JOIN tenmo_user as tu\n" +
                "ON a.user_id = tu.user_id\n" +
                "JOIN transfer_type as tt\n" +
                "ON t.transfer_type_id = tt.transfer_type_id\n" +
                "JOIN transfer_status as ts\n" +
                "ON t.transfer_status_id = ts.transfer_status_id\n" +
                "WHERE transfer_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        if (result.next())
        {
            Transfer transfer = mapRowToTransfer(result);
            var transferStatus = result.getString("transfer_status_desc");
            var transferType = result.getString("transfer_type_desc");

            transfer.setTransferStatus(transferStatus);
            transfer.setTransferType(transferType);

            return transfer;
        }
        else
        {
            return null;
        }
    }

    @Override
    public Integer addTransfer(Transfer transfer)
    {
        Integer transferId;
        int userFrom = transfer.getAccountFrom();
        int userTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        int typeId = transfer.getTransferTypeId();
        int statusId = transfer.getTransferStatusId();

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (?,\n" +
                "        ?,\n" +
                "        (SELECT account_id FROM account WHERE user_id = ?), " +
                "(SELECT account_id FROM account WHERE user_id = ?), " +
                "?) RETURNING transfer_id;";

        transferId = jdbcTemplate.queryForObject(sql, Integer.class, typeId, statusId, userFrom, userTo, amount);

        if (transferId != null)
        { return transferId;}

        return null;
    }

    @Override
    public void updateTransfer(Transfer transfer)
    {
        Integer transferId = transfer.getTransferId();
        int typeId = transfer.getTransferTypeId();
        int statusId = transfer.getTransferStatusId();
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();

        String sql = "UPDATE transfer SET transfer_id = ? " +
                " , transfer_type_id = ? " +
                " , transfer_status_id = ? " +
                " , account_from = ? " +
                " , account_to = ? " +
                " , amount = ? "  +
                " WHERE transfer_id = ?;";

        jdbcTemplate.update(sql, transferId, typeId, statusId, accountFrom, accountTo, amount, transferId);
    }


    private Transfer mapRowToTransfer(SqlRowSet rs)
    {
        var transferId = rs.getInt("transfer_id");
        var transferTypeId = rs.getInt("transfer_type_id");
        var transferStatusId = rs.getInt("transfer_status_id");
        var accountFrom = rs.getInt("account_from");
        var accountTo = rs.getInt("account_to");
        var amount = rs.getBigDecimal("amount");
        var username = rs.getString("username");
        var transfer = new Transfer(){{
            setTransferId(transferId);
            setTransferTypeId(transferTypeId);
            setTransferStatusId(transferStatusId);
            setAccountFrom(accountFrom);
            setAccountTo(accountTo);
            setAmount(amount);
            setUsername(username);
        }};
        return transfer;
    }
}
