package com.calindra.tech.backend;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.calindra.tech.backend.exceptions.InternalErrorException;
import com.calindra.tech.backend.model.Address;
import com.calindra.tech.backend.model.AddressResponse;
import com.calindra.tech.backend.service.BaseMaps;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource("classpath:/application.properties")
class BackendTechApplicationTests extends BaseMaps {

	
	@LocalServerPort
	private int localPort;

	@Test
	public void test_address_controller() {

		try {
			
			final ObjectMapper mapper = new ObjectMapper();
			
			final List<Address> addresses = super.listAddress();
			
			final String json = mapper.writeValueAsString(addresses);
		
			final ArrayList<AddressResponse> responses = RestAssured.given()
					.port(this.localPort).with()
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.body(json)
					.request("POST", "/address")
					.as(ArrayList.class);
			
			Assert.assertNotNull(responses);
			Assert.assertEquals(addresses.size(), addresses.size());
			Assert.assertFalse(responses.isEmpty());
			
			
			final ObjectMapper mapperResponses = new ObjectMapper();
			
			final String jsonResponses = mapperResponses.writeValueAsString(responses);
			
			final ObjectMapper mapperExpected = new ObjectMapper();
			
			final List<AddressResponse> listResponse = super.listAddressResponse();
			
			final String jsonExpected = mapperExpected.writeValueAsString(listResponse);
			
			Assert.assertEquals(jsonExpected, jsonResponses);
			
		}catch(Exception e ) {
			
			log.error("Controller RestAssured error: ", e);
			throw new InternalErrorException("Controller RestAssured error");
			
		}
		
		
			
	}
	
	
	

}

