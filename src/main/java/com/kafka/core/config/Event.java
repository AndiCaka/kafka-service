package com.kafka.core.config;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.kafka.core.dto.GeoNetDTO;

public class Event {

	private final GeoNetDTO geoNetModel;
	private final Function<String, String> assessmentFunction;
	private final BiConsumer<Integer, String> updateFunction;

	public Event(GeoNetDTO geoNetModel, Function<String, String> assessmentFunction,
			BiConsumer<Integer, String> updateFunction) {
		super();
		this.geoNetModel = geoNetModel;
		this.assessmentFunction = assessmentFunction;
		this.updateFunction = updateFunction;
	}

	public GeoNetDTO getGeoNetModel() {
		return geoNetModel;
	}

	public Function<String, String> assess() {
		return assessmentFunction;
	}

	public BiConsumer<Integer, String> updateDOI() {
		return updateFunction;
	}

}
