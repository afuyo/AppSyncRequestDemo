package com.appsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public  class ApiKeyExample {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new KotlinModule());
    private static final String API_KEY_HEADER = "x-api-key";
    private final HttpClient client;
    /**
     * AWS AppSync API credentials
     */
    private final String apiKey;
    private final String region;
    private final String api;

    public ApiKeyExample(final String apiKey, final String region, final String api) {
        this.apiKey = apiKey;
        this.region = region;
        this.api = api;
        this.client = HttpClientBuilder
                .create()
                .setDefaultHeaders(newArrayList(new BasicHeader(API_KEY_HEADER, this.apiKey)))
                .build();
    }

    private HttpPost getHttpPost(final String endpointUrl, final Map<String, String> headers) {
        final URI uri;
        try {
            uri = new URI(endpointUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        final HttpPost request = new HttpPost(uri);

        if(null != headers && !headers.isEmpty()){
            for(Map.Entry<String, String> e : headers.entrySet()){
                request.addHeader(e.getKey(), e.getValue());
            }
        }

        request.setConfig(RequestConfig.custom().setConnectionRequestTimeout(30).build());

        return request;
    }

    private HttpEntity prepareHttpEntity(final GraphQLRequest graphQLRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", graphQLRequest.getQuery());
        requestBody.put("variables", graphQLRequest.getVariables());

        if (graphQLRequest.getOperationName() != null) {
            requestBody.put("operationName", graphQLRequest.getOperationName());
        }

        try {
            return new StringEntity(MAPPER.writeValueAsString(requestBody), ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private GraphQLResult readResult(final InputStream inputStream) {
        try {
            return MAPPER.readValue(inputStream, GraphQLResult.class);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public GraphQLResult executePost(final AppSyncRequest appSyncRequest) {
        final HttpPost httpPost = getHttpPost(api, null);
        final HttpEntity httpEntity = prepareHttpEntity(appSyncRequest.getGraphqlRequest());
        httpPost.setEntity(httpEntity);

        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(httpPost);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        InputStream content;
        try {
            content = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return readResult(content);
    }
}