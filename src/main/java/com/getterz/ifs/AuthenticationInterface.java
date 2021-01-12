package com.getterz.ifs;

import com.getterz.network.Header;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationInterface<Req,Res> {

    Header<Res> signup(@RequestBody Header<Req> request);

    Header<Res> login(@RequestBody Header<Req> request);

    Header<Res> logout(@RequestBody Header<Req> request);

}
