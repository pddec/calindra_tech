package com.calindra.tech.backend.functions;

import com.calindra.tech.backend.exceptions.InternalErrorException;
import com.calindra.tech.backend.model.Address;
import com.calindra.tech.backend.model.AddressPoints;
import com.calindra.tech.backend.model.AddressResponse;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class AddressResponseFunctions {

	public static Function<AddressPoints, AddressResponse> createResponses() {

		return addressPoint -> {
			try {
				final List<AddressPoints> addressPoints = addressPoint.getAddressPoints();

				final Supplier<Integer> size = () -> {
					if (addressPoints.size() > 1)
						return addressPoints.size() - 2;
					return addressPoints.size() - 1;
				};

				final Address longest = addressPoints.get(0).getAddress();
				final Address closest = addressPoints.get(size.get()).getAddress();

				return AddressResponse.builder().addressPoints(addressPoint).closest(closest).longest(longest).build();

			} catch (Exception e) {
				log.error("Response error", e);
				throw new InternalErrorException("Response error", e);
			}
		};
	}
}
