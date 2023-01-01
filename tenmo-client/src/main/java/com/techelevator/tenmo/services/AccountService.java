package com.techelevator.tenmo.services;

import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.models.Account;
import java.math.BigDecimal;

public class AccountService extends AuthenticationService<Account>
{
    private static final String API_BASE_URL = "http://localhost:8080/account/";

    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url)
    {
        super(url);
    }

    public Account getAccount(int accountId)
    {
        try
        {
            var url = API_BASE_URL;
            var entity = makeAuthEntity();
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            return response.getBody();
        }
        catch (Exception ex)
        {
            BasicLogger.log(ex.getMessage());
            return null;
        }
    }

    public BigDecimal getAccountBalance()
    {
        try
        {// get balance details from API
            var url = API_BASE_URL + "balance";
            var entity = makeAuthEntity();
            // return balance
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, entity, BigDecimal.class);
            return response.getBody();
        }
        catch (Exception ex)
        {
            BasicLogger.log(ex.getMessage());
            return null;
        }
    }

    private HttpEntity<Account> createAccountEntity(Account account)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(account, headers);
    }
}
