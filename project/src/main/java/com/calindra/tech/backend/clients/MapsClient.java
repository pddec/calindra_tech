package com.calindra.tech.backend.clients;

import com.calindra.tech.backend.model.MapsResponse;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

public interface MapsClient {
    @RequestLine("GET")
    MapsResponse getAddressComponents(@QueryMap() final Map<String,String> queryParam);
}
