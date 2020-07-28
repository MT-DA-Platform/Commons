package com.MTDap.commons.db.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionProvider {
    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private String port;

    //@Bean
    public RestHighLevelClient getConnection() {
        HttpHost[] httpHosts = {new HttpHost(host, Integer.parseInt(port))};
        RestClientBuilder clientBuilder = RestClient.builder(httpHosts);
        return new RestHighLevelClient(clientBuilder);
    }
}
