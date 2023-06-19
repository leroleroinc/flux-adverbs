package com.lerolero.adverbs.services;

import java.time.Duration;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import com.lerolero.adverbs.repositories.MongoAdverbRepository;
import com.lerolero.adverbs.models.Adverb;

@Service
public class AdverbService {

	@Autowired
	private MongoAdverbRepository repo;

	private Mono<String> next() {
		return repo.pullRandom()
			.map(a -> a.getString());
	}

	public Mono<String> randomAdverb() {
		return next()
			.subscribeOn(Schedulers.boundedElastic());
	}

	public Flux<String> randomAdverbList(Integer size) {
		return Flux.range(1, size)
			.flatMap(i -> next())
			.subscribeOn(Schedulers.boundedElastic());
	}

	public Flux<String> randomAdverbProducer(Integer interval) {
		return Flux.interval(Duration.ofMillis(interval))
			.flatMap(i -> next())
			.subscribeOn(Schedulers.boundedElastic());
	}

}
