package com.sf.integration.api;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sf.integration.model.ConnectionOAuth2;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-09-06T14:14:17.501Z")

@Controller
public class SfdcApiController implements SfdcApi {

	@Autowired
	SalesforceRestClient salesforceRestClient;

	@Override
	public ResponseEntity<String> getSObjects(
			@NotNull @ApiParam(required = true, value = "Connection") @RequestBody ConnectionOAuth2 connectionOAuth2,
			@NotNull @ApiParam(required = true, value = "SOQL Query") @RequestParam String soql) throws Exception {
		String response = salesforceRestClient.getSObjects(connectionOAuth2, soql);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> upsertSobjects(
			@NotNull @ApiParam(required = true, value = "Connection") @RequestBody ConnectionOAuth2 connectionOAuth2, String json, String sobjectType,
			String id) throws Exception {
		String response = salesforceRestClient.upsertSObjects(connectionOAuth2, json, sobjectType.concat(id));
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> createSObjects(
			@NotNull @ApiParam(required = true, value = "Connection") @RequestBody ConnectionOAuth2 connectionOAuth2, String sobjectType, String json)
			throws Exception {
		String response = salesforceRestClient.createSObjects(connectionOAuth2, json, sobjectType);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

}
