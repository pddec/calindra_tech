package com.calindra.tech.backend.functions;

import com.calindra.tech.backend.clients.MapsClient;
import com.calindra.tech.backend.model.Address;
import com.calindra.tech.backend.model.AddressPoints;
import com.calindra.tech.backend.model.MapsResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.calindra.tech.backend.model.MapsResponse.AddressComponents;
import static com.calindra.tech.backend.model.MapsResponse.Results;

public class MapServicesFunctions {

    public static Function<Address, MapsResponse> mapResponse(final MapsClient mapsClient,final String apiKey){
        return address -> {
            final Map<String,String> queryParam = Map.of("address",address.toURL(),"key",apiKey);

            return mapsClient.getAddressComponents(queryParam);
        };
    }

    public static Function<Results, AddressPoints> mapPoints() {
        return result -> {
            final List<AddressComponents> addressComponents = result.getAddress_components();

            final Address address = Address.Type.fromAddressComponents(addressComponents);

            final Map<String,String> location = result.getGeometry().getLocation();

            final String lng = location.get("lng");

            final String lat = location.get("lat");

            return AddressPoints.builder()
                    .address(address)
                    .lng(Double.valueOf(lng))
                    .lat(Double.valueOf(lat))
                    .build();
        };
    }
}
