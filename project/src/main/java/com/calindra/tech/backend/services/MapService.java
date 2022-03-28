package com.calindra.tech.backend.services;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.calindra.tech.backend.clients.MapsClient;
import com.calindra.tech.backend.functions.AddressPointsFunctions;
import com.calindra.tech.backend.functions.AddressResponseFunctions;
import com.calindra.tech.backend.functions.MapServicesFunctions;
import com.calindra.tech.backend.model.Address;
import com.calindra.tech.backend.model.AddressPoints;
import com.calindra.tech.backend.model.AddressResponse;
import com.calindra.tech.backend.model.MapsResponse;
import com.calindra.tech.backend.model.MapsResponse.Results;


@Service
public class MapService {

    @Value("${apikey}")
    private String apiKey;

    @Autowired
    private MapsClient mapsClient;

    public List<AddressResponse> postAddresses(final List<Address> addresses){

        final Function<Address, MapsResponse> mapResponse = MapServicesFunctions.mapResponse(this.mapsClient,this.apiKey);
        final Function<Results, AddressPoints> mapAddressPoints = MapServicesFunctions.mapPoints();

        final List<AddressPoints> addressPoints = addresses.stream()
                .map(mapResponse)
                .map(MapsResponse::getResults)
                .flatMap(List::stream)
                .map(mapAddressPoints)
                .collect(Collectors.toList());

        final Function<AddressPoints, AddressPoints> calcDistances = AddressPointsFunctions.calcDistances(addressPoints);
        final Function<AddressPoints, AddressResponse> response = AddressResponseFunctions.createResponses();

        final List<AddressResponse> listPoints = addressPoints.parallelStream()
                .map(calcDistances)
                .map(response)
                .collect(Collectors.toList());

        return listPoints;
    }

}
