package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.views.BalancePage;
import com.techelevator.tenmo.views.SendPage;
import com.techelevator.tenmo.views.UserOutput;
import org.springframework.web.client.RestTemplate;

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

    private AuthenticatedUser currentUser;
    private Account currentAccount;

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
                continue;
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
        // TODO Auto-generated method stub
        currentAccount = accountService.getAccount(currentUser.getUser().getId());
       BigDecimal balance = accountService.getAccountBalance();
       currentAccount.setBalance(balance);
       // print balance
        BalancePage balancePage = new BalancePage();
        balancePage.displayBalance(balance);
    }

    private void viewTransferHistory()
    {
        // TODO Auto-generated method stub

    }

    private void viewPendingRequests()
    {
        // TODO Auto-generated method stub
    }

    private void sendBucks()
    {
        // list all available users for transfer
        List<User> availableUsers = userService.getAllAvailableUsers();
        SendPage sendPage = new SendPage();
        sendPage.displayAvailableUsers(availableUsers);
        // Request ID & amount

        int selectedId = userOutput.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
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

            transferService.updateBalances(newTransfer);
        }
//
//                //TODO:  create Transfer object
//            }
//            else
//            {
//                System.out.println("You cannot make transfers with yourself. Enter a different accountId");
//            }


    }

    private void requestBucks()
    {
        // TODO Auto-generated method stub

    }

}
