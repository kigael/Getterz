package com.getterz.service.adminApi;

import com.getterz.domain.model.Job;
import com.getterz.domain.repository.JobRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.network.Header;
import com.getterz.network.request.JobApiRequest;
import com.getterz.network.response.JobApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobApiLogicService extends CrudService<JobApiRequest, JobApiResponse, Job> {

    @Autowired
    private final JobRepository jobRepository;

    @Override
    public Header<JobApiResponse> create(Header<JobApiRequest> request) {
        String transactionType = "JOB CREATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            JobApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getName()==null){
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                if(FormatCheck.jobName(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(jobRepository.findFirstByName(body.getName()).isPresent()) return Header.ERROR(transactionType,"DUPLICATE NAME",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(jobRepository.save(Job.builder()
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
    public Header<JobApiResponse> read(Long id, String session) {
        String transactionType = "JOB READ";
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
                        .map(job -> Header.OK(transactionType,response(job),SessionApi.updateSession(session)))
                        .orElseGet(()->Header.ERROR(transactionType,"JOB NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    @Override
    public Header<JobApiResponse> update(Header<JobApiRequest> request) {
        String transactionType = "JOB UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            JobApiRequest body = request.getData();
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
                Optional<Job> job = baseRepository.findById(body.getId());
                if(job.isEmpty()) return Header.ERROR(transactionType,"JOB NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.jobName(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(jobRepository.findFirstByName(body.getName()).isPresent()) return Header.ERROR(transactionType,"DUPLICATE NAME",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(baseRepository.save(job.get()
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
        String transactionType = "JOB DELETE";
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
                        .map(job -> {
                            baseRepository.delete(job);
                            return Header.OK(transactionType,SessionApi.updateSession(session));
                        })
                        .orElseGet(()->Header.ERROR(transactionType,"JOB NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    private JobApiResponse response(Job job){
        return JobApiResponse.builder()
                .id(job.getId())
                .name(job.getName())
                .build();
    }

    public static JobApiResponse Body(Job job){
        return JobApiResponse.builder()
                .id(job.getId())
                .name(job.getName())
                .build();
    }

}
