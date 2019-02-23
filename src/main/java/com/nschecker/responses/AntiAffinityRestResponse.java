package com.nschecker.responses;

import com.nschecker.dtos.AntiAffinityRestDto;

public class AntiAffinityRestResponse {

	boolean created;
	String message;
	Response<AntiAffinityRestDto> jsondata; 
	ConsistencyResponse consistency;
	
	public AntiAffinityRestResponse() {
		this.created = false;
		this.message = "default message";
		this.jsondata = new Response<AntiAffinityRestDto>();
		this.consistency = new ConsistencyResponse();
	}
	
	public boolean isCreated() {
		return created;
	}
	
	public void setCreated(boolean created) {
		this.created = created;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}	
	
	public Response<AntiAffinityRestDto> getJsondata() {
		return jsondata;
	}

	public void setJsondata(Response<AntiAffinityRestDto> jsondata) {
		this.jsondata = jsondata;
	}

	public ConsistencyResponse getConsistency() {
		return consistency;
	}
	
	public void setConsistency(ConsistencyResponse consistency) {
		this.consistency = consistency;
	}		
	
}
