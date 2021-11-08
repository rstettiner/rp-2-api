package com.rafa.service;

import com.rafa.base.SystemMessages;
import com.rafa.domain.auditable.dto.UserLoginDto;
import com.rafa.repository.base.UserRepository;
import com.rafa.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class UserService {

    @Autowired
    private ApplicationContext ctx;

    private UserService self;

    @PostConstruct
    public void init() {
        // Creating reference to instance. Necessary to call cached methods from withing class.
        self = ctx.getBean(UserService.class);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void verifyLogin(UserLoginDto userLoginDto){
        if(userLoginDto.getUsername() == null || userLoginDto.getUsername().length() <= 0){
            log.error("Empty username passed to service");
            throw new ServiceException(SystemMessages.User.EMPTY_USERNAME, null);
        } if(userLoginDto.getPassword() == null || userLoginDto.getPassword().length() <= 0){
            log.error("Empty password passed to service");
            throw new ServiceException(SystemMessages.User.EMPTY_PASSWORD, null);
        }
        //todo logic to verify login
    }

}
