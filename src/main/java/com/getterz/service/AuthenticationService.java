package com.getterz.service;

import com.getterz.ifs.AuthenticationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class AuthenticationService<Req, Res, Entity> implements AuthenticationInterface<Req, Res> {

    @Autowired(required = false)
    protected JpaRepository<Entity,Long> baseRepository;

}
