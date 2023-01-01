package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

import java.util.List;

public class PendingRequestPage extends BasePage
{
    public void displayPendingRequest(List<Transfer> transfers, User currentUser)
    {
        printCyan("------------------------------------------------------------------------" + "\n");
        printCyan("Transfer ID" + "         " +  "Transfers To"  + "         " + "Amount($)" + "\n");
        printCyan("------------------------------------------------------------------------" + "\n");
        for (Transfer transfer: transfers) {
            boolean isCurrentUser = transfer.getToUserName().equalsIgnoreCase(currentUser.getUsername());
            boolean isARequest = transfer.getTransferTypeId() == 1;
            boolean isPending = transfer.getTransferStatusId() == 1;
            if (!isCurrentUser && isARequest && isPending) {
                printCyan(transfer.getTransferId() + "         " + transfer.getToUserName() + "         " + transfer.getAmount() + "\n");
            }
        }
        printCyanLine("------------------------------------------------------------------------");
        printCyanLine("1. Select Request");
        printCyanLine("0. Exit.");
    }

    public void displayRequestApproved(int transferId)
    {
        printCyan("Request " + transferId);
        printGreenLine(" APPROVED.");
    }

    public void displayRequestRejected(int transferId)
    {
        printCyan("Request " + transferId);
        printRedLine(" APPROVED.");
    }

}
