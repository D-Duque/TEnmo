package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService extends AuthenticationService<User>
{
    private static final String API_BASE_URL = "http://localhost:8080/users/";

    private final RestTemplate restTemplate = new RestTemplate();

    public UserService(String url)
    {
        super(url);
    }

    public List<User> getAllUsers() {
        List<User> users = null;
        try {
            var url = API_BASE_URL;
            var entity = makeAuthEntity();
            // return balance
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            users = Arrays.asList(response.getBody());
        }
        catch (Exception ex)
        {
            BasicLogger.log(ex.getMessage());
        }
        return users;
    }

    public List<User> getAllAvailableUsers() {
        List<User> users = null;
        try {
            var url = API_BASE_URL + "transferlist";
            var entity = makeAuthEntity();
            // return balance
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            users = Arrays.asList(response.getBody());
        }
        catch (Exception ex)
        {
            BasicLogger.log(ex.getMessage());
        }
        return users;
    }

}
