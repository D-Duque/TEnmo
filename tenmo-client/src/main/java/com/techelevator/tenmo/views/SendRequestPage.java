package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.User;

import java.util.List;

public class SendRequestPage extends BasePage
{

    public void displayAvailableUsers(List<User> users)
    {
        printLine("User ID" + "          " + "Name");
        for (User user : users)
        {
            printCyan(user.getId() + "          " + user.getUsername() + "\n");
        }
    }
}



