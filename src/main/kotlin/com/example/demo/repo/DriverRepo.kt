package com.example.demo.repo

import com.example.demo.model.Driver
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor


interface DriverRepo : MongoRepository<Driver, String>, QuerydslPredicateExecutor<Driver> {
    fun findBySurname(value: String): Driver
}