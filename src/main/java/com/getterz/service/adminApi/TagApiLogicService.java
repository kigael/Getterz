package com.getterz.service.adminApi;

import com.getterz.domain.model.Tag;
import com.getterz.domain.repository.TagRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.network.Header;
import com.getterz.network.request.TagApiRequest;
import com.getterz.network.response.TagApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagApiLogicService extends CrudService<TagApiRequest, TagApiResponse, Tag> {

    @Autowired
    private final TagRepository tagRepository;

    @Override
    public Header<TagApiResponse> create(Header<TagApiRequest> request) {
        String transactionType = "TAG CREATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            TagApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getName()==null){
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                if(FormatCheck.tagName(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(tagRepository.findByName(body.getName()).isPresent()) return Header.ERROR(transactionType,"DUPLICATE NAME",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(tagRepository.save(Tag.builder()
                        .name(body.getName())
                        .build())),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header<TagApiResponse> read(Long id, String session) {
        String transactionType = "TAG READ";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(id==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                return baseRepository.findById(id)
                        .map(tag -> Header.OK(transactionType,response(tag),SessionApi.updateSession(session)))
                        .orElseGet(()->Header.ERROR(transactionType,"TAG NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    @Override
    public Header<TagApiResponse> update(Header<TagApiRequest> request) {
        String transactionType = "TAG UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            TagApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getName()==null){
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Tag> tag = baseRepository.findById(body.getId());
                if(tag.isEmpty()) return Header.ERROR(transactionType,"TAG NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.tagName(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(tagRepository.findByName(body.getName()).isPresent()) return Header.ERROR(transactionType,"DUPLICATE NAME",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(baseRepository.save(tag.get()
                        .setName(body.getName()))),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header delete(Long id, String session) {
        String transactionType = "TAG DELETE";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(id==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                return baseRepository.findById(id)
                        .map(tag -> {
                            baseRepository.delete(tag);
                            return Header.OK(transactionType,SessionApi.updateSession(session));
                        })
                        .orElseGet(()->Header.ERROR(transactionType,"TAG NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    private TagApiResponse response(Tag tag){
        return TagApiResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public static TagApiResponse Body(Tag tag){
        return TagApiResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

}
