package com.ssf.abdm.services;

import com.ssf.abdm.constant.RequestType;
import com.ssf.abdm.entities.Response;
import com.ssf.abdm.exception.EntityNotFoundException;
import com.ssf.abdm.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ABDMService implements IABDMService {

    @Autowired
    private ResponseRepository repository;

    @Override
    public void persistResponse(Map<String, Object> data, RequestType type) {
        String requestId = data.containsKey("resp") ? ((Map<String, Object>) data.get("resp")).containsKey("requestId") ? ((Map<String, Object>) data.get("resp")).get("requestId").toString() : "" : "";
        repository.save(Response.builder().requestId(requestId).data(data).type(type).build());
    }

    @Override
    public Response getResponse(String requestId) {
        Optional<Response> optional = repository.findByRequestId(requestId);
        if (optional.isPresent()) {
            repository.deleteById(optional.get().getId());
            return optional.get();
        } else {
            throw new EntityNotFoundException("Data not found for requestId: " + requestId);
        }
    }

}
