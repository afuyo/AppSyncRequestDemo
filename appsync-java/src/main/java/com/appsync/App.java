package com.appsync;

/**
 * Hello world!
 *
 */

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableMap;



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
