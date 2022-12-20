package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TransferService extends AuthenticationService<Transfer>
{
    private static final String API_BASE_URL = "http://localhost:8080/transfer/";

    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url)
    {
        super(url);
    }

    // create transfer object
    public boolean updateBalances(Transfer transfer)
    {
        HttpEntity<Transfer> entity = makeAuthEntity(transfer);
        boolean success = false;
        try
        {
            restTemplate.exchange(API_BASE_URL, HttpMethod.PUT, entity, Transfer.class);
            success = true;
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Transfer> createTransferEntity(Transfer transfer)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(transfer, headers);
    }

    // retrieve transfer history
    public List<Transfer> getTransferHistory() {
        List<Transfer> transfers = null;
        try {
            var url = API_BASE_URL + "history";
            var entity = makeAuthEntity();
            // return transfer list
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        }
        catch (Exception ex)
        {
            BasicLogger.log(ex.getMessage());
        }
        return transfers;
    }

    // retrieve transfer details by transfer id
    public Transfer getTransferDetail(int transferId)
    {
        try
        {
            var url = API_BASE_URL + transferId;
            var entity = makeAuthEntity();
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer.class);
            return response.getBody();
        }
        catch (Exception ex)
        {
            BasicLogger.log(ex.getMessage());
            return null;
        }
    }

}
