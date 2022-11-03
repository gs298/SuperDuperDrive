package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {

    private UserMapper userMapper;
    private HashService hashService;

    @Autowired
    public AuthenticationService(UserMapper userMapper, HashService hashService){

        this.userMapper = userMapper;
        this.hashService = hashService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Users user;

        try{
            user = userMapper.getUser(username);
        }catch (IllegalArgumentException e){
            return null;
        }

        String passwordComp = hashService.getHashedValue(password, user.getSalt());

        if(user.getPassword().equals(passwordComp)){
            return new UsernamePasswordAuthenticationToken(username, passwordComp, new ArrayList<>());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {

        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    public Authentication getLoggedInUser(){
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        return authenticatedUser;
    }
}

