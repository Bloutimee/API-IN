package com.iodefaction.api.common.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConnection {
    private @Getter
    final String databaseName;
    private @Getter
    final MongoClient mongoClient;

    public MongoConnection(MongoCredentials mongoCredentials) {
        this.databaseName = mongoCredentials.getDatabase();

        MongoCredential credential = MongoCredential.createCredential(mongoCredentials.getUser(), mongoCredentials.getDatabase(), mongoCredentials.getPassword().toCharArray());
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .build();
        this.mongoClient = new MongoClient(new ServerAddress(mongoCredentials.getHost(), mongoCredentials.getPort()), (Arrays.asList(credential)));
    }
}
