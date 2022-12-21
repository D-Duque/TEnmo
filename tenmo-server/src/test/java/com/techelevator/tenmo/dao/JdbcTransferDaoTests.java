package com.techelevator.tenmo.dao;

import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDaoTests extends BaseDaoTests
{
    protected static final Transfer TRANSFER_1 = new Transfer(3001, 2, 2, 2001, 2002, new BigDecimal("500.00"));
    protected static final Transfer TRANSFER_2 = new Transfer(3002, 2, 2, 2001, 2002, new BigDecimal("500.00"));
    protected static final Transfer TRANSFER_3 = new Transfer(3003, 1, 2, 2002, 2001, new BigDecimal("500.00"));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);

        TRANSFER_1.setAccountFrom(1001);
        TRANSFER_1.setAccountTo(1002);
        TRANSFER_2.setAccountFrom(1001);
        TRANSFER_2.setAccountTo(1002);

        sut.addTransfer(TRANSFER_1);
        sut.addTransfer(TRANSFER_2);
    }

    @Test
    public void addTransfer_CreatesATransferInTransferTable()
    {
        // arrange
        Integer expectedId = 3001;
        // act
        Integer actualId = TRANSFER_1.getTransferId();
        // assert
        assertNotNull(actualId);
        assertEquals("Because function should create a new transfer with ID 3001", expectedId, actualId);
    }

    @Test
    public void findAll_ShouldReturnListOfTransfers()
    {
        // arrange
        int expectedListSize = 2;

        // act
        int actualListSize = sut.findAll(2001).size();
        // assert
        assertEquals("Because findAll should return a list of all transfers available to that account", expectedListSize, actualListSize);

    }

    @Test
    public void getTransferById_ShouldReturnTransferForSpecificId()
    {
        // arrange
        Transfer expected = TRANSFER_1;
        expected.setAccountFrom(2001);
        expected.setAccountTo(2002);


        // act
        Transfer actual = sut.getTransferById(3001);

        // assert
        assertNotNull(actual);
        assertEquals(expected.getTransferId(), actual.getTransferId());
        assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        assertEquals(expected.getAccountTo(), actual.getAccountTo());
        assertEquals(expected.getAmount(), actual.getAmount());
    }
}
