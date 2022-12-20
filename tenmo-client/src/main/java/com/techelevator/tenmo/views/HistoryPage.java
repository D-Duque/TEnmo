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
        printLine("Transfer ID" + "         " +  "Request type" + "                    " + "From/To" + "                    " + "Amount");
        for (Transfer transfer: transfers) {
            if (transfer.getTransferTypeId() == 1)
            {
                transfer.setTransferType("Request");
            } else
            {
                transfer.setTransferType("Send");
            }

            if (transfer.getTransferType().equalsIgnoreCase("Send"))
            {
                if (transfer.getAccountFrom() != account.getAccountId())
                {
                    printCyan(transfer.getTransferId() + "                    " + transfer.getTransferType() + "                    From:" + transfer.getFromUsername() + "                    " + transfer.getAmount() + "\n");
                } else if (transfer.getAccountTo() != account.getAccountId())
                {
                    printCyan(transfer.getTransferId() + "                    " + transfer.getTransferType() + "                    To:" + transfer.getToUserName() + "                    " + transfer.getAmount() + "\n");
                }
            }
            else
            {
                if (transfer.getAccountFrom() != account.getAccountId())
                {
                    printCyan(transfer.getTransferId() + "                    " + transfer.getTransferType() + "                    To:" + transfer.getFromUsername() + "                    " + transfer.getAmount() + "\n");
                } else if (transfer.getAccountTo() != account.getAccountId())
                {
                    printCyan(transfer.getTransferId() + "                    " + transfer.getTransferType() + "                    From:" + transfer.getToUserName() + "                    " + transfer.getAmount() + "\n");
                }
            }

        }
    }
}
