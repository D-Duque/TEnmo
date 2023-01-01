package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.Transfer;

public class TransferDetailPage extends BasePage
{
    public void displayTransferDetail(Transfer transfer)
    {
        printCyan("------------------------------------------------------------" + "\n");
        printCyan("Transfer Details");
        printCyan("------------------------------------------------------------" + "\n");
        printCyan("Id: " + transfer.getTransferId() + "\n");
        printCyan("From: " + transfer.getFromUsername() + "\n");
        printCyan("To: " + transfer.getToUserName() + "\n");
        printCyan("Type: " + transfer.getTransferType() + "\n");
        printCyan("Status: " + transfer.getTransferStatus() + "\n");
        printCyan("Amount: $" + transfer.getAmount() + "\n");
    }
}

