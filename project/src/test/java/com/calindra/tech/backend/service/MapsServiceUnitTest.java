package com.calindra.tech.backend.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.calindra.tech.backend.clients.MapsClient;
import com.calindra.tech.backend.exceptions.InternalErrorException;
import com.calindra.tech.backend.functions.AddressPointsFunctions;
import com.calindra.tech.backend.functions.AddressResponseFunctions;
import com.calindra.tech.backend.functions.MapServicesFunctions;
import com.calindra.tech.backend.model.Address;
import com.calindra.tech.backend.model.AddressPoints;
import com.calindra.tech.backend.model.AddressResponse;
import com.calindra.tech.backend.model.MapsResponse;
import com.calindra.tech.backend.model.MapsResponse.Results;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:/application.properties")
public class MapsServiceUnitTest extends BaseMaps {

	@Value("${apikey}")
	private String apiKey;

	@Autowired
	private MapsClient mapsClient;

	@Test
	public void test_google_api_call() {

		final Address address = Address.builder().streetName("Amphitheatre Parkway").neighbourhood("Mountain View")
				.number("1600").state("CA").country("United States").zipcode("94043").build();

		final Address addressExpected = address.toBuilder()
				.city("Santa Clara County")
				.state("California").build();
		
		log.info(address.toURL());
		
		final Function<Results, AddressPoints> mapAddressPoints = MapServicesFunctions.mapPoints();

		final List<AddressPoints> addressPoints = List.of(AddressPoints.builder().address(addressExpected)
				.lat(Double.valueOf("37.422388")).lng(Double.valueOf("-122.0841883")).build());

		final Map<String, String> queryParam = Map.of("address", address.toURL(), "key", this.apiKey);

		final MapsResponse results = this.mapsClient.getAddressComponents(queryParam);

		final List<AddressPoints> addressPointsList = results.getResults().stream().map(mapAddressPoints)
				.collect(Collectors.toList());

		Assert.assertEquals(1, addressPointsList.size());
		Assert.assertEquals(addressPoints, addressPointsList);

	}

	@Test
	public void test_calc_distances_response() {

		final Function<Address, MapsResponse> mapResponse = MapServicesFunctions.mapResponse(this.mapsClient,
				this.apiKey);
		
		final Function<Results, AddressPoints> mapAddressPoints = MapServicesFunctions.mapPoints();

		final List<AddressPoints> addressPoints = super.listAddress().stream().map(mapResponse)
				.map(MapsResponse::getResults).flatMap(List::stream).map(mapAddressPoints)
				.collect(Collectors.toList());

		final Function<AddressPoints, AddressPoints> calcDistances = AddressPointsFunctions
				.calcDistances(addressPoints);
		final Function<AddressPoints, AddressResponse> response = AddressResponseFunctions.createResponses();

		final List<AddressResponse> listPoints = addressPoints.parallelStream().map(calcDistances).map(response)
				.collect(Collectors.toList());
		try {
			final ObjectMapper mapperLog = new ObjectMapper();
			mapperLog.enable(SerializationFeature.INDENT_OUTPUT);
			final String jsonLog = mapperLog.writeValueAsString(listPoints);

			log.info(jsonLog);
			
			final List<AddressResponse> listExpected = super.listAddressResponse();
			
			final ObjectMapper mapperExpected = new ObjectMapper();
			mapperExpected.enable(SerializationFeature.INDENT_OUTPUT);
			final String jsonExpected = mapperLog.writeValueAsString(listExpected);
			
			log.info(jsonExpected);
			
			Assert.assertEquals(jsonExpected, jsonLog);
			
			Assert.assertEquals(4, listPoints.size());

		} catch (JsonProcessingException e) {
			log.error("JSON RESPONSE TEST ", e);
			throw new InternalErrorException("JSON RESPONSE TEST ", e);
		}
	}
}
