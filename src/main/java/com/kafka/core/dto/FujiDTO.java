package com.kafka.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FujiDTO {

	@JsonProperty("metadata_service_endpoint")
	private final String metadataServiceEndpoint;
	@JsonProperty("metadata_service_type")
	private final String metadataServiceType;
	@JsonProperty("object_identifier")
	private final String objectIdentifier;
	@JsonProperty("test_debug")
	private final boolean testDebug;
	@JsonProperty("use_datacite")
	private final boolean useDatacite;

	public FujiDTO(String metadataServiceEndpoint, String metadataServiceType, String objectIdentifier,
			boolean testDebug, boolean useDatacite) {
		super();
		this.metadataServiceEndpoint = metadataServiceEndpoint;
		this.metadataServiceType = metadataServiceType;
		this.objectIdentifier = objectIdentifier;
		this.testDebug = testDebug;
		this.useDatacite = useDatacite;
	}
}
