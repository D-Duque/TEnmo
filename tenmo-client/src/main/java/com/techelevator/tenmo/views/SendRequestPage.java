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

    public void displaySendResult(Boolean isSuccessful, BigDecimal amount, int id)
    {
        if (isSuccessful)
        {
           printGreenLine("\nYour transfer of " + amount + " TE bucks to user " + id + " was successful!");
        }
        else
        {
            printRedLine("\nYour transfer of " + amount + " TE bucks to user " + id + " failed.");
        }
    }

    public void displayRequestResult(Boolean isSuccessful, BigDecimal amount, int id)
    {
        if (isSuccessful)
        {
            printGreenLine("\nYour request of " + amount + " TE bucks was sent to user " + id + " successfully!");
        }
        else
        {
            printRedLine("\nYour request of " + amount + " TE bucks to user " + id + " failed.");
        }
    }
}



