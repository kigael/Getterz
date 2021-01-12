package com.getterz.ifs;

import com.getterz.network.Header;

public interface CrudInterface<Req,Res> {

    Header<Res> create(Header<Req> request);

    Header<Res> read(Long id, String session);

    Header<Res> update(Header<Req> request);

    Header delete(Long id, String session);

}
