package com.kafka.core.dto;

public class GeoNetDTO {

	// TODO add missing fields and rename existing to geonetwork metadatadoi table column names
	private Integer id;
	private String doiURL;
	private String assessment;

	public Integer getId() {
		return id;
	}

	public String getDoiURL() {
		return doiURL;
	}

	public void setDoiURL(String doiURL) {
		this.doiURL = doiURL;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}
}
