package com.example.demo.repo

import com.example.demo.model.Driver
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

//@Repository
interface ReactiveDriverRepo : ReactiveMongoRepository<Driver, String> {

    fun findBySurname(s: String): Mono<Driver>
}