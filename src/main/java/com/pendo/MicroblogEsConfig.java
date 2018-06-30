package com.pendo;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.pendo.microblog.repo")
public class MicroblogEsConfig {

    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    public String getEsHost() {
    	return EsHost;
    }
    
    @Bean
    public Client client() throws Exception {
    	
    	Settings settings = Settings.builder()
                 .put("cluster.name", EsClusterName)
                 .build();
    	
    	return new PreBuiltTransportClient(settings)
                 .addTransportAddress(new TransportAddress(InetAddress.getByName(EsHost), Integer.valueOf(EsPort)));
    	 
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }


    //Embedded Elasticsearch Server
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchTemplate();
//    }

}