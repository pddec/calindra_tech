package com.calindra.tech.backend.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.calindra.tech.backend.functions.AddressPointsFunctions;
import com.calindra.tech.backend.functions.AddressResponseFunctions;
import com.calindra.tech.backend.model.Address;
import com.calindra.tech.backend.model.AddressPoints;
import com.calindra.tech.backend.model.AddressResponse;

public class BaseMaps {

	protected List<Address> listAddress() {
		/*
		 * "location": { "lat": -22.766756, "lng": -47.1570997 }
		 */
		final Address staCecilia = Address.builder().streetName("R. Santa Cecília").neighbourhood("Santa Cecilia")
				.city("Paulínia").state("SP").country("Brazil").zipcode("13140-358").build();

		/*
		 * "location": { "lat": -22.9024416, "lng": -47.0627188 }
		 */
		final Address avGlicerio = Address.builder().number("1419").streetName("R. José Paulino")
				.neighbourhood("Centro").city("Campinas").state("SP").country("Brazil").zipcode("13013-001").build();

		/*
		 * "location": { "lat": -23.5162023, "lng": -46.62423279999999 }
		 */
		final Address terTiete = Address.builder().number("1800").streetName("Av. Cruzeiro do Sul")
				.neighbourhood("Santana").city("São Paulo").state("SP").country("Brazil").zipcode("02030-000").build();

		/*
		 * "location": { "lat": -22.9563012, "lng": -43.1772916 }
		 */
		final Address lauroMuller = Address.builder().number("116").streetName("Rua Lauro Müller")
				.neighbourhood("Botafogo").city("Rio de Janeiro").state("RJ").country("Brazil").zipcode("22250-040")
				.build();

		return List.of(staCecilia, avGlicerio, terTiete, lauroMuller);
	}

	protected List<AddressResponse> listAddressResponse() {
		final Address staCecilia = Address.builder().streetName("Rua Santa Cecília").neighbourhood("Santa Cecilia")
				.city("Paulínia").state("São Paulo").country("Brazil").zipcode("13140-358").build();

		final Address avGlicerio = Address.builder().number("1419").streetName("Rua José Paulino")
				.neighbourhood("Centro").city("Campinas").state("São Paulo").country("Brazil").zipcode("13013-001").build();

		final Address terTiete = Address.builder().number("1800").streetName("Avenida Cruzeiro do Sul")
				.neighbourhood("Santana").city("São Paulo").state("São Paulo").country("Brazil").zipcode("02030-000").build();

		final Address lauroMuller = Address.builder().number("116").streetName("Rua Lauro Müller")
				.neighbourhood("Botafogo").city("Rio de Janeiro").state("Rio de Janeiro").country("Brazil").zipcode("22290-160")
				.build();

		final AddressPoints staCeciliaPoints = AddressPoints.builder().address(staCecilia)
				.lat(Double.valueOf("-22.766756")).lng(Double.valueOf("-47.1570997")).build();

		final AddressPoints avGlicerioPoints = AddressPoints.builder().address(avGlicerio)
				.lat(Double.valueOf("-22.9024416")).lng(Double.valueOf("-47.0627188")).build();

		final AddressPoints terTietePoints = AddressPoints.builder().address(terTiete)
				.lat(Double.valueOf("-23.5162023")).lng(Double.valueOf("-46.62423279999999")).build();

		final AddressPoints lauroMullerPoints = AddressPoints.builder().address(lauroMuller)
				.lat(Double.valueOf("-22.9542949")).lng(Double.valueOf("-43.177554")).build();

		final List<AddressPoints> listAddressPoints = List.of(staCeciliaPoints, avGlicerioPoints, terTietePoints,
				lauroMullerPoints);

		final Function<AddressPoints, AddressPoints> calcDistances = AddressPointsFunctions
				.calcDistances(listAddressPoints);
		final Function<AddressPoints, AddressResponse> response = AddressResponseFunctions.createResponses();

		return listAddressPoints.parallelStream().map(calcDistances).map(response).collect(Collectors.toList());
	}

}
