package com.nhermens.auth.persist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;


@Configuration
@EnableReactiveMongoRepositories(basePackages={"com.nhermens.auth"})
@Profile({"dev","prod"})
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {

	@Value("${spring.data.mongo.host}")
	private String host;
	
	@Value("${spring.data.mongo.port}")
	private Integer port;
	
	@Override
	protected String getDatabaseName() {
		return "ReactiveAuthDB";
	}

	@Override
	public MongoClient reactiveMongoClient() {
		return MongoClients.create(String.format("mongodb://%s:%d", host, port));
	}


}
