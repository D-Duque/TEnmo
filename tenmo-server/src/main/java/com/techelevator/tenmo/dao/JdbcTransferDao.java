package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

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
}
