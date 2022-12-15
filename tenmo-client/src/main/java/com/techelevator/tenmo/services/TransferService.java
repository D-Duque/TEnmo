package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import org.springframework.web.client.RestTemplate;

public class TransferService extends AuthenticationService<Transfer>
{
    private static final String API_BASE_URL = "http://localhost:8080/transfer/";

    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url)
    {
        super(url);
    }
}
