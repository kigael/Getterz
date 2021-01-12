package com.getterz.controller;

import com.getterz.ifs.CrudInterface;
import com.getterz.network.Header;
import com.getterz.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
public abstract class CrudController<Req,Res,Entity> implements CrudInterface<Req,Res> {

    @Autowired(required = false)
    protected CrudService<Req,Res,Entity> baseService;

    @Override
    @PostMapping("")
    public Header<Res> create(@RequestBody Header<Req> request) {
        return baseService.create(request);
    }

    @Override
    @GetMapping("")
    public Header<Res> read(@RequestParam(value = "id")Long id, @RequestParam(value = "getterz-session")String session) {
        return baseService.read(id,session);
    }

    @Override
    @PutMapping("")
    public Header<Res> update(@RequestBody Header<Req> request) {
        return baseService.update(request);
    }

    @Override
    @DeleteMapping("")
    public Header delete(@RequestParam(value = "id")Long id, @RequestParam(value = "getterz-session")String session) {
        return baseService.delete(id,session);
    }
}
