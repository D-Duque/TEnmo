package com.techelevator.tenmo.views;

import java.math.BigDecimal;

public class BalancePage extends BasePage
{
    public void displayBalance(BigDecimal balance)
    {
        printCyan("Your account balance is: " + balance);
    }
}
