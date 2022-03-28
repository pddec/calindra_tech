package com.calindra.tech.backend.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.util.StringUtils;

import com.calindra.tech.backend.functions.AddressFunctions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@JsonSerialize
@Builder(toBuilder = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String streetName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String neighbourhood;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String state;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String country;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String city;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String number;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String zipcode;

	public boolean hasCEP() {
		return StringUtils.hasText(this.zipcode);
	}

	public boolean hasNumber() {
		return StringUtils.hasText(this.number);
	}

	public boolean hasBairro() {
		return StringUtils.hasText(this.neighbourhood);
	}

	public boolean hasCidade() {
		return StringUtils.hasText(this.city);
	}

	public String toURL() {
		final StringBuilder sb = new StringBuilder();

		if (this.hasNumber())
			sb.append(this.number).append("+");

		sb.append(this.streetName.replaceAll(" ", "+"));

		if (this.hasBairro())
			sb.append("+").append(this.neighbourhood.replaceAll(" ", "+"));

		sb.append(",");

		if (this.hasCidade())
			sb.append(this.city.replaceAll(" ", "+")).append("+");

		sb.append(this.state.replaceAll(" ", "+")).append("+");

		if (this.hasCEP())
			sb.append(this.zipcode);

		sb.append(",").append(this.country.replaceAll(" ", "+"));

		return sb.toString();
	}

	public enum Type {

		NEIGHBOURHOOD("politicalsublocalitysublocality_level_1","localitypolitical"), 
		ZIPCODE("postal_code"),
		CITY("administrative_area_level_2political"), 
		STATE("administrative_area_level_1political"), 
		NUMBER("street_number"),
		COUNTRY("countrypolitical"), 
		STREET("route"),
		UNKNOWN("");

		@Getter
		private List<String> names;
		@Setter
		private String value;

		Type(final String... types) {
			this.names = Arrays.asList(types);
		}

		public boolean hasTypeFrom(final String type) {
			return this.names.contains(type);
		}

		public AddressBuilder buildAddress(Address.AddressBuilder builder) {
			if (Type.STREET.equals(this))
				return builder.streetName(this.value);
			if (Type.NEIGHBOURHOOD.equals(this))
				return builder.neighbourhood(this.value);
			if (Type.CITY.equals(this))
				return builder.city(this.value);
			if (Type.STATE.equals(this))
				return builder.state(this.value);
			if (Type.COUNTRY.equals(this))
				return builder.country(this.value);
			if (Type.NUMBER.equals(this))
				return builder.number(this.value);
			return builder.zipcode(this.value);
		}

		private boolean isUnknown() {
			return this.equals(Type.UNKNOWN);
		}

		private boolean nonUnknown() {
			return !this.isUnknown();
		}

		public static Address fromAddressComponents(List<MapsResponse.AddressComponents> addressComponents) {

			final AddressBuilder builder = Address.builder();

			final Consumer<Type> each = AddressFunctions.eachTypes(builder);

			addressComponents.parallelStream().map(Type::fromAddressComponent)
			.filter(Type::nonUnknown).forEach(each);

			return builder.build();

		}

		private static Type fromAddressComponent(MapsResponse.AddressComponents addressComponent) {
			final List<Type> types = Arrays.asList(Type.values());

			final String typeName = addressComponent.getTypes().stream()
					.reduce(new StringBuilder(),AddressFunctions.accomulatorTypes(),AddressFunctions.combinatorTypes())
					.toString();

			final Type mapped = types.parallelStream()
					.filter(type -> type.hasTypeFrom(typeName))
					.distinct().findAny()
					.orElse(Type.UNKNOWN);

			mapped.setValue(addressComponent.getLong_name());
			return mapped;
		}
	}

}
