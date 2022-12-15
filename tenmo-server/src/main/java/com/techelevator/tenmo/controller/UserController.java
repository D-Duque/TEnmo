package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController
{
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping(value = "")
    public List<User> findAll()
    {
        List<User> users = userDao.findAll();
        return users;
    }

    @GetMapping(value = "/transferlist")
    public List<User> findAllExceptCurrentUser(Principal principal)
    {
        List<User> users = userDao.findAllExceptCurrentUser(principal.getName());
        return users;
    }
 }
