package com.techelevator.tenmo.dao;

import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests
{
    protected static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal("1000"));
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal("1000"));
    protected static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal("1000"));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void findAll_Should_Return_List_Of_AllAccounts() {
        // arrange & act
        List<Account> accounts = sut.findAll();
        //assert
        Assert.assertNotNull(accounts);
        Assert.assertEquals(3, accounts.size());
        Assert.assertEquals(ACCOUNT_1, accounts.get(0));
        Assert.assertEquals(ACCOUNT_2, accounts.get(1));
        Assert.assertEquals(ACCOUNT_3, accounts.get(2));
    }

    @Test
    public void getAccountByAccountId()
    {
        // arrange
        Account expected = ACCOUNT_1;
        // act
        Account actual = sut.getAccountByAccountId(2001);
        // assert
        Assert.assertEquals("Because get account by account id should retrieve the associated account.", expected, actual);
    }




}
