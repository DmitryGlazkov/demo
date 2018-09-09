package com.example.demo.repo

import com.example.demo.model.Driver
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

//@Repository
interface ReactiveDriverRepo
    : ReactiveMongoRepository<Driver, String>//, QuerydslPredicateExecutor<Driver>