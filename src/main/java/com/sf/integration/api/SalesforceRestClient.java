package com.sf.integration.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sf.integration.model.ConnectionOAuth2;

@Service
public class SalesforceRestClient {

	@Value("${sfdc.url.path}")
	private String urlPath;

	private final String RELATIVE_PATH = "sobjects/";

	
	public String getSObjects(ConnectionOAuth2 connectionOAuth2, String soql) throws Exception {
		// login

		final JsonNode loginResult = login(connectionOAuth2);
		// parse

		final String accessToken = loginResult.get("access_token").asText();
		final String instanceUrl = loginResult.get("instance_url").asText();

		final URIBuilder builder = new URIBuilder(instanceUrl);
		builder.setPath(urlPath.concat("query")).setParameter("q", soql);

		final HttpGet get = new HttpGet(builder.build());
		get.setHeader("Authorization", "Bearer " + accessToken);

		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final HttpResponse queryResponse = httpclient.execute(get);

		final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		final JsonNode queryResults = mapper.readValue(queryResponse.getEntity().getContent(), JsonNode.class);

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResults);
	}

	public String createSObjects(ConnectionOAuth2 connectionOAuth2, String json, String resourceUri) throws Exception {
		final JsonNode loginResult = login(connectionOAuth2);
		final String accessToken = loginResult.get("access_token").asText();
		final String instanceUrl = loginResult.get("instance_url").asText();

		StringBuilder url = new StringBuilder(urlPath);
		url.append(RELATIVE_PATH);
		url.append(resourceUri);

		final URIBuilder builder = new URIBuilder(instanceUrl);
		builder.setPath(url.toString());

		final HttpPost httpPost = new HttpPost(builder.build());
		httpPost.setHeader("Authorization", "Bearer " + accessToken);

		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final CloseableHttpResponse queryResponse = httpclient.execute(httpPost);

		final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		final JsonNode queryResults = mapper.readValue(queryResponse.getEntity().getContent(), JsonNode.class);

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResults);
	}
	
	
	public String upsertSObjects(ConnectionOAuth2 connectionOAuth2, String json, String resourceUri) throws Exception {
		final JsonNode loginResult = login(connectionOAuth2);
		final String accessToken = loginResult.get("access_token").asText();
		final String instanceUrl = loginResult.get("instance_url").asText();

		StringBuilder url = new StringBuilder(urlPath);
		url.append(RELATIVE_PATH);
		url.append(resourceUri);

		final URIBuilder builder = new URIBuilder(instanceUrl);
		builder.setPath(url.toString());

		final HttpPatch httpPost = new HttpPatch(builder.build());
		httpPost.setHeader("Authorization", "Bearer " + accessToken);

		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final CloseableHttpResponse queryResponse = httpclient.execute(httpPost);

		final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		final JsonNode queryResults = mapper.readValue(queryResponse.getEntity().getContent(), JsonNode.class);

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResults);
	}

	/**
	 * 
	 * @param connectionOAuth2
	 * @return
	 * @throws Exception
	 */
	private JsonNode login(ConnectionOAuth2 connectionOAuth2) throws Exception {
		// login
		final CloseableHttpClient httpclient = HttpClients.createDefault();

		final List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
		loginParams.add(new BasicNameValuePair("client_id", connectionOAuth2.getClientId()));
		loginParams.add(new BasicNameValuePair("client_secret", connectionOAuth2.getClientSecret()));
		loginParams.add(new BasicNameValuePair("grant_type", "password"));
		loginParams.add(new BasicNameValuePair("username", connectionOAuth2.getUserId()));
		loginParams.add(new BasicNameValuePair("password", connectionOAuth2.getPassword()));

		final HttpPost post = new HttpPost(connectionOAuth2.getUrl());
		post.setEntity(new UrlEncodedFormEntity(loginParams));

		final HttpResponse loginResponse = httpclient.execute(post);

		// parse
		final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

		final JsonNode loginResult = mapper.readValue(loginResponse.getEntity().getContent(), JsonNode.class);

		return loginResult;
	}

}
