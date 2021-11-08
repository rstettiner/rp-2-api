package com.rafa.web.rest;

import com.rafa.domain.auditable.dto.UserLoginDto;
import com.rafa.service.UserService;
import com.rafa.web.base.AbstractBaseResource;
import com.rafa.web.base.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserResource extends AbstractBaseResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) { this.userService = userService; }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ServiceResponse<String> login(@RequestBody UserLoginDto login) {

        log.info("Received a GET /api/user/login service call: [{}]", login);

        this.userService.verifyLogin(login);

        return success("Usuário logado com sucesso");
    }
}
