package com.getterz.ifs;

import com.getterz.network.Header;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationInterface<Req,Res> {

    Header<Res> signup(@RequestBody Header<Req> request);

    Header<Res> verifyImageUpload(@RequestParam("verifyImage") MultipartFile verifyImage, @RequestParam("fileName") String fileName);

    Header<Res> profileImageUpload(@RequestParam("profileImage") MultipartFile profileImage, @RequestParam("fileName") String fileName);

    Header<Res> login(@RequestBody Header<Req> request);

    Header<Res> logout(@RequestBody Header<Req> request);

    Header<Res> verifyEmail(@RequestParam String token);

}
