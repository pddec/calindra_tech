package com.calindra.tech.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.calindra.tech.backend.exceptions.InternalErrorException;
import com.calindra.tech.backend.model.Address;
import com.calindra.tech.backend.model.AddressResponse;
import com.calindra.tech.backend.services.MapService;


@Controller
@RequestMapping("/address")
public class MapsAddressController {
	
	@Autowired
	private MapService mapService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AddressResponse> postAddresses(@RequestBody List<Address> address) {
		
		if(address.isEmpty())
			throw new InternalErrorException("There's no elements");
		
		return mapService.postAddresses(address);
	}

}
