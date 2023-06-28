package com.lerolero.adverbs.repositories;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RBucketReactive;

import reactor.core.publisher.Mono;

import com.lerolero.adverbs.repositories.MongoAdverbRepository;
import com.lerolero.adverbs.models.Adverb;

@Repository
public class AdverbCache {

	@Autowired
	private RedissonReactiveClient redis;

	@Autowired
	private MongoAdverbRepository repo;

	private List<String> ids;

	public Mono<Adverb> next() {
		if (ids == null || ids.size() == 0) {
			ids = new ArrayList<>();
			repo.findAll().map(Adverb::getId).doOnNext(i -> ids.add(i)).subscribe();
		}
		Adverb defaultAdverb = new Adverb();
		return Mono.fromSupplier(() -> ids.get((int)(Math.random() * ids.size())))
			.doOnNext(id -> defaultAdverb.setId(id))
			.flatMap(id -> redis.getBucket("/adverb/" + id).get())
			.cast(Adverb.class)
			.defaultIfEmpty(defaultAdverb);
	}

	public void add(Adverb adverb) {
		RBucketReactive<Adverb> bucket = redis.getBucket("/adverb/" + adverb.getId());
		bucket.set(adverb).subscribe();
		ids.add(adverb.getId());
	}

}
