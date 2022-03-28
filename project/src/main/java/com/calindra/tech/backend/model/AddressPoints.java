package com.calindra.tech.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonSerialize
@EqualsAndHashCode
@Builder(toBuilder = true)
public class AddressPoints {

    private Address address;
    private Double lat;
    private Double lng;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double distance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AddressPoints> addressPoints;

    public Double calcDistance(final AddressPoints that){

        final Double lat = that.lat - this.lat;
        final Double lng = that.lng - this.lng;

        final Double powLat = Math.pow(lat,2);
        final Double powLng = Math.pow(lng,2);

        final Double sum = powLat+powLng;

        return Math.sqrt(sum);
    }



}
