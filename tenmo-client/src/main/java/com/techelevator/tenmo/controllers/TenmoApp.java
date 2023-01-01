package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.views.*;

import java.math.BigDecimal;
import java.util.List;

public class TenmoApp
{
    private static final String API_BASE_URL = "http://localhost:8080/";

    private final UserOutput userOutput = new UserOutput();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    final int ST_APPROVED = 2;
    final int ST_REJECT = 3;

    private AuthenticatedUser currentUser;
    private Account currentAccount;

    SendRequestPage sendRequestPage = new SendRequestPage();
    PendingRequestPage pendingRequestPage = new PendingRequestPage();

    public void run()
    {
        userOutput.printGreeting();
        loginMenu();
        if (currentUser != null)
        {
            mainMenu();
        }
    }

    private void loginMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null)
        {
            userOutput.printLoginMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                handleRegister();
            }
            else if (menuSelection == 2)
            {
                handleLogin();
            }
            else if (menuSelection != 0)
            {
                System.out.println("Invalid Selection");
                userOutput.pause();
            }
        }
    }

    private void handleRegister()
    {
        System.out.println("Please register a new user account");
        UserCredentials credentials = userOutput.promptForCredentials();
        if (authenticationService.register(credentials))
        {
            System.out.println("Registration successful. You can now login.");
        }
        else
        {
            userOutput.printErrorMessage();
        }
    }

    private void handleLogin()
    {
        UserCredentials credentials = userOutput.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser != null) {
            AuthenticationService.setAuthToken(currentUser.getToken());
        } else {
            userOutput.printErrorMessage();
        }
    }

    private void mainMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0)
        {
            userOutput.printMainMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                viewCurrentBalance();
            }
            else if (menuSelection == 2)
            {
                viewTransferHistory();
                continue;
            }
            else if (menuSelection == 3)
            {
                viewPendingRequests();
            }
            else if (menuSelection == 4)
            {
                sendBucks();
            }
            else if (menuSelection == 5)
            {
                requestBucks();
            }
            else if (menuSelection == 0)
            {
                System.exit(0);
            }
            else
            {
                System.out.println("Invalid Selection");
            }
            userOutput.pause();
        }
    }

    private void viewCurrentBalance()
    {
        BigDecimal balance = accountService.getAccountBalance();
        // print balance
        BalancePage balancePage = new BalancePage();
        balancePage.displayBalance(balance);
    }

    private void viewTransferHistory()
    {
        List<Transfer> transfers = transferService.getTransferHistory();
        currentAccount = accountService.getAccount(currentUser.getUser().getId());
        HistoryPage historyPage = new HistoryPage();

        // prompt user for transfer menu option
        int menuSelection = -1;

        while (menuSelection != 0)
        {
            historyPage.displayTransferHistory(currentAccount, transfers, currentUser.getUser());
            userOutput.printTransferMenu();
            menuSelection = userOutput.promptForInt("Please choose an option: ");
            if (menuSelection == 1)
            {
                int transferId = userOutput.promptForInt("Enter transfer Id: ");
                viewTransferById(transferId);
                userOutput.pause();
            }
            else if (menuSelection == 0)
            {
                mainMenu();
            }
            else
            {
                System.out.println("Not a valid selection.");
            }
        }
    }

    private void viewTransferById(int transferId) {
        Transfer transfer = transferService.getTransferDetail(transferId);
        TransferDetailPage detailPage = new TransferDetailPage();
        detailPage.displayTransferDetail(transfer);
    }

    private void sendBucks()
    {
        // list all available users for transfer
        displayUserList();
        // Request ID & amount
        int selectedId = userOutput.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        if (selectedId == 0) {mainMenu();}
        BigDecimal selectedAmount = userOutput.promptForBigDecimal("Enter amount: ");
        boolean hasEnoughMoney = selectedAmount.compareTo(accountService.getAccountBalance()) <= 0;
        boolean isMoreThanZero = selectedAmount.compareTo(BigDecimal.ZERO) > 0;

        // verify selectedAmount < currentBalance
        if (!hasEnoughMoney)
        {
            System.out.println("You don't have enough funds to make this transfer.");
            sendBucks();
        }
        else if (!isMoreThanZero)
        {
            System.out.println("You need to enter an amount greater than 0 to transfer.");
            sendBucks();
        }
        else
        {
            // create transfer object
            Transfer newTransfer = new Transfer(){{
                setAccountFrom(currentUser.getUser().getId());
                setAccountTo(selectedId);
                setAmount(selectedAmount);
            }};

            boolean isSuccessful = transferService.updateBalances(newTransfer);
            sendRequestPage.displaySendResult(isSuccessful, selectedAmount, selectedId);
        }
    }

    private void requestBucks()
    {
        displayUserList();
        // Request ID & amount
        int selectedId = userOutput.promptForInt("Enter ID of user you are requesting bucks from (0 to cancel): ");
        if (selectedId == 0) {mainMenu();}
        BigDecimal selectedAmount = userOutput.promptForBigDecimal("Enter amount: ");

        boolean isMoreThanZero = selectedAmount.compareTo(BigDecimal.ZERO) > 0;

        // verify selectedAmount < currentBalance
        if (!isMoreThanZero)
        {
            System.out.println("You need to enter an amount greater than 0 to transfer.");
            requestBucks();
        }
        else
        {
            // create transfer object
            Transfer newTransfer = new Transfer()
            {{
                setAccountFrom(selectedId);
                setAccountTo(currentUser.getUser().getId());
                setAmount(selectedAmount);
            }};
            boolean isSuccessful = transferService.createRequest(newTransfer);
            sendRequestPage.displayRequestResult(isSuccessful, selectedAmount, selectedId);
        }
    }

    private void displayUserList() {
        List<User> availableUsers = userService.getAllAvailableUsers();
        sendRequestPage.displayAvailableUsers(availableUsers);
    }

    private void viewPendingRequests()
    {
        final int VIEW_REQUEST = 1;
        final int EXIT_REQUEST = 0;
        List<Transfer> transfers = transferService.getTransferHistory();

        currentAccount = accountService.getAccount(currentUser.getUser().getId());
        pendingRequestPage.displayPendingRequest(transfers, currentUser.getUser());

        int menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");

        // Approve/Reject Transfer
        if (menuSelection == VIEW_REQUEST)
        {
            approveRejectRequest();
        }
        else if (menuSelection == EXIT_REQUEST)
        {
            mainMenu();
        }
        else
        {
            System.out.println("Not a valid menu option.");
        }
    }

    private void approveRejectRequest()
    {
        final int APPROVE = 1;
        final int REJECT = 2;
        //retrieve the transfer object based on user choice of Id
        int transferId = userOutput.promptForInt("Enter transfer Id: ");
        Transfer request = transferService.getTransferDetail(transferId);
        userOutput.printRequestMenu();
        int menuSelection = userOutput.promptForInt("Please choose an option: ");

        //option1: to approve
        if (menuSelection == APPROVE) {

            request.setTransferStatusId(ST_APPROVED);
            //to update DAO
            boolean isSuccessful = transferService.updateRequest(request);
            if (isSuccessful) {pendingRequestPage.displayRequestApproved(transferId);}
            else {pendingRequestPage.displayFailure("Approval");}
        }
        else if (menuSelection == REJECT)
        {
            request.setTransferStatusId(ST_REJECT);
            //to update DAO
            boolean isSuccessful = transferService.updateRequest(request);
            if (isSuccessful) {pendingRequestPage.displayRequestRejected(transferId);}
            else {pendingRequestPage.displayFailure("Rejection");}
        }
        else {
            mainMenu();
        }
    }
}

