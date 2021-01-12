package com.getterz.controller;

import com.getterz.ifs.AuthenticationInterface;
import com.getterz.network.Header;
import com.getterz.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
public abstract class AuthenticationController<Req, Res, Entity> implements AuthenticationInterface<Req, Res> {

    @Autowired(required = false)
    protected AuthenticationService<Req,Res,Entity> baseService;

    @Override
    @PostMapping("signup")
    public Header<Res> signup(Header<Req> request) {
        return baseService.signup(request);
    }

    @Override
    @PostMapping("login")
    public Header<Res> login(Header<Req> request) {
        return baseService.login(request);
    }

    @Override
    @PostMapping("logout")
    public Header<Res> logout(Header<Req> request) {
        return baseService.logout(request);
    }

}
