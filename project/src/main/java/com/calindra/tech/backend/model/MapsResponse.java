package com.calindra.tech.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@Builder
@ToString
@AllArgsConstructor
public class MapsResponse {

    private List<Results> results;
    private String status;

    @Data
    @Builder
    @ToString
    @AllArgsConstructor
    public static class AddressComponents{
        private String long_name;
        private String short_name;
        private List<String> types;
    }

    @Data
    @Builder
    @ToString
    @AllArgsConstructor
    public static class Geometry{
        private Map<String,String> location;
        private String location_type;
        private Map<String,Map<String,String>> viewport;
    }

    @Data
    @Builder
    @ToString
    @AllArgsConstructor
    public static class Results{
        private List<AddressComponents> address_components;
        private String formatted_address;
        private Geometry geometry;
        private String place_id;
        private Map<String,String> plus_code;
        private List<String> types;
    }

}
