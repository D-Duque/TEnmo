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
    protected static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal("1000.00"));
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal("1000.00"));
    protected static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal("1000.00"));

    private JdbcAccountDao sut;

    private Account testAccount;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
        testAccount = new Account(2004, 1004, new BigDecimal("1000.00"));
    }

    @Test
    public void findAll_Should_Return_List_Of_AllAccounts() {
        // arrange & act
        List<Account> accounts = sut.findAll();
        //assert
        Assert.assertNotNull(accounts);
        Assert.assertEquals(3, accounts.size());
        Assert.assertEquals(ACCOUNT_1.getAccountId(), accounts.get(0).getAccountId());
        Assert.assertEquals(ACCOUNT_1.getUserId(), accounts.get(0).getUserId());
        Assert.assertEquals(ACCOUNT_1.getBalance(), accounts.get(0).getBalance());

        Assert.assertEquals(ACCOUNT_2.getAccountId(), accounts.get(1).getAccountId());
        Assert.assertEquals(ACCOUNT_2.getUserId(), accounts.get(1).getUserId());
        Assert.assertEquals(ACCOUNT_2.getBalance(), accounts.get(1).getBalance());

        Assert.assertEquals(ACCOUNT_3.getAccountId(), accounts.get(2).getAccountId());
        Assert.assertEquals(ACCOUNT_3.getUserId(), accounts.get(2).getUserId());
        Assert.assertEquals(ACCOUNT_3.getBalance(), accounts.get(2).getBalance());
    }

    @Test
    public void getAccountByAccountId_Should_Return_Correct_Account_For_AccountId_2001()
    {
        // arrange
        Account expected = ACCOUNT_1;
        int accountId = 2001;
        // act
        Account actual = sut.getAccountByAccountId(accountId);
        // assert
        Assert.assertEquals("Because get account by account id should retrieve the associated account.", expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }

    @Test
    public void getAccountById_Should_Return_Correct_Account_For_UserId_1001() {
        // arrange
        int userId = 1001;
        Account expect = ACCOUNT_1;
        // act
        Account actual = sut.getAccountById(userId);
        // assert
        String message = "Because get account by user id should retrieve the associated account.";
        Assert.assertEquals(message, expect.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expect.getUserId(), actual.getUserId());
        Assert.assertEquals(expect.getBalance(), actual.getBalance());
    }

}
