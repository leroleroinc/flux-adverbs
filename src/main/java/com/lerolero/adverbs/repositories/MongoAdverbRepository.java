package com.lerolero.adverbs.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;

import reactor.core.publisher.Mono;

import com.lerolero.adverbs.models.Adverb;

public interface MongoAdverbRepository extends ReactiveMongoRepository<Adverb,String> {

	@Aggregation("{ $sample: { size: 1 } }")
	public Mono<Adverb> pullRandom();

}
