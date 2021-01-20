package com.getterz.controller;

import com.getterz.ifs.AuthenticationInterface;
import com.getterz.network.Header;
import com.getterz.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Component
public abstract class AuthenticationController<Req, Res, Entity> implements AuthenticationInterface<Req, Res> {

    @Autowired(required = false)
    protected AuthenticationService<Req,Res,Entity> baseService;

    @Override
    @PostMapping("signup")
    public Header<Res> signup(@RequestBody Header<Req> request) {
        return baseService.signup(request);
    }

    @Override
    @PostMapping("verify_image_upload")
    public Header<Res> verifyImageUpload(
            @RequestParam("verifyImage") MultipartFile verifyImage,
            @RequestParam("fileName") String fileName) {
        return baseService.verifyImageUpload(verifyImage,fileName);
    }

    @Override
    @PostMapping("profile_image_upload")
    public Header<Res> profileImageUpload(
            @RequestParam("profileImage") MultipartFile profileImage,
            @RequestParam("fileName") String fileName) {
        return baseService.profileImageUpload(profileImage,fileName);
    }

    @Override
    @PostMapping("login")
    public Header<Res> login(@RequestBody Header<Req> request) {
        return baseService.login(request);
    }

    @Override
    @PostMapping("logout")
    public Header<Res> logout(@RequestBody Header<Req> request) {
        return baseService.logout(request);
    }

    @Override
    @GetMapping("verify_email")
    public Header<Res> verifyEmail(@RequestParam String token) {
        return baseService.verifyEmail(token);
    }

}
