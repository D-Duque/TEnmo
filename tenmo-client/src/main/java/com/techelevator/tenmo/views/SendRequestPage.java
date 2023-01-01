package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.User;

import java.math.BigDecimal;
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

    public void displaySendSuccess(BigDecimal amount, int id)
    {
        printGreenLine("\nYour transfer of " + amount + " TE bucks to user " + id + " was successful!");
    }

    public void displaySendFailure(BigDecimal amount, int id)
    {
        printRedLine("\nYour transfer of " + amount + " TE bucks to user " + id + " failed.");
    }

    public void displayRequestSuccess(BigDecimal amount, int id)
    {
        printGreenLine("\nYour request of " + amount + " TE bucks was sent to user " + id + " successfully!");
    }

    public void displayRequestFailure(BigDecimal amount, int id)
    {
        printRedLine("\nYour request of " + amount + " TE bucks to user " + id + " failed.");
    }
}



