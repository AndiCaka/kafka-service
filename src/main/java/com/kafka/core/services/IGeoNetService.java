package com.kafka.core.services;

import com.kafka.core.dto.GeoNetDTO;

import java.util.List;

public interface IGeoNetService {

	void process();
	public List<GeoNetDTO> getDoiList();
}
