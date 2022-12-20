package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

import java.util.List;

public class PendingRequestPage extends BasePage
{
    public void displayPendingRequest(Account account, List<Transfer> transfers, User currentUser)
    {
        printCyan("------------------------------------------------------------------------" + "\n");
        printCyan("Transfer ID" + "         " +  "Transfers To"  + "         " + "Amount($)" + "\n");
        printCyan("------------------------------------------------------------------------" + "\n");
        for (Transfer transfer: transfers) {
            boolean isNotCurrentUser = transfer.getUsername().equalsIgnoreCase(currentUser.getUsername());
            boolean isARequest = transfer.getTransferTypeId() == 1;
            if (!isNotCurrentUser && isARequest) {
                printCyan(transfer.getTransferId() + "         " + transfer.getUsername() + "         " + transfer.getAmount() + "\n");
            }
        }
    }
}
