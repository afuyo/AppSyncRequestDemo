package com.appsync;

/**
 * Hello world!
 *
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.common.collect.ImmutableMap;

import static com.google.common.collect.Lists.newArrayList;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * AWS AppSync API credentials
     */
    private static final String apiKey = "da2-zamf7t2jfzfbnbwxnvstizbtoy";
    private static final String region = "us-east-1";
    private static final String api = "https://gk2nokygznfvnlerwai6xxsv2i.appsync-api.us-east-1.amazonaws.com/graphql";


    public static void main(String[] args) {
        ApiKeyExample apiKeyExample = new ApiKeyExample(apiKey, region, api);

        String mutation = " mutation PutPost {createPost(input:{title:\"HTTP HELLO\"}) {id title } }";
        Map<String, String> variables = ImmutableMap.of("id", "411", "title", "Hello HTTP World!");
        String operationName = "PutPost";

        GraphQLRequest graphQLRequest = new GraphQLRequest(mutation, variables, operationName);
        System.out.println(graphQLRequest.toString());
        AppSyncRequest appSyncRequest = new AppSyncRequest(graphQLRequest);
        System.out.println(appSyncRequest.toString());

        GraphQLResult graphQLResult = apiKeyExample.executePost(appSyncRequest);

        log.info("result {}", graphQLResult);
    }


}
