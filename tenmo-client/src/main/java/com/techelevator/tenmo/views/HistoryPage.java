package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;

import java.util.List;

public class HistoryPage extends BasePage
{
    public void displayTransferHistory(Account account, List<Transfer> transfers, User currentUser)
    {
        printLine("Transfer ID" + "         " + "From/To" + "         " + "Amount");
        for (Transfer transfer: transfers) {
            if (!transfer.getUsername().equalsIgnoreCase(currentUser.getUsername()))
            {
                if (transfer.getAccountFrom() != account.getAccountId())
                {
                    printCyan(transfer.getTransferId() + "          From:" + transfer.getUsername() + "          " + transfer.getAmount() + "\n");
                } else if (transfer.getAccountTo() != account.getAccountId())
                {
                    printCyan(transfer.getTransferId() + "            To:" + transfer.getUsername() + "          " + transfer.getAmount() + "\n");
                }
            }
        }
    }
}
