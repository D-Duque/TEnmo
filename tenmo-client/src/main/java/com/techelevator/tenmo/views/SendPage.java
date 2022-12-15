package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.User;

import java.math.BigDecimal;
import java.util.List;

public class SendPage extends BasePage
{
    public void displayAvailableUsers(List<User> users)
    {
        for (User user: users) {
            printLine("User ID" + "          " + "Name");
            printCyan(user.getId() + "          " + user.getUsername() + "\n");
        }
    }
}
