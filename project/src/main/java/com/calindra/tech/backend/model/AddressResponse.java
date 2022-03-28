package com.calindra.tech.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonSerialize
@Builder(toBuilder = true)
public class AddressResponse {

    private AddressPoints addressPoints;
    private Address longest;
    private Address closest;

}
