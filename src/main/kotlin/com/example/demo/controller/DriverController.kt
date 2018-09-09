package com.example.demo.controller

import com.example.demo.model.Driver
import com.example.demo.model.QDriver
import com.example.demo.repo.DriverRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.domain.Sort

@RestController
class DriverController {

    @Autowired
    private lateinit var driverRepo: DriverRepo

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @RequestMapping(method = [RequestMethod.GET], value = ["/drivers"])
    fun findAll(): ResponseEntity<*> = ResponseEntity.ok(driverRepo.findAll())

    @RequestMapping(method = [RequestMethod.GET], value = ["/nameZ"])
    fun findDrivers(): ResponseEntity<*> {
        val qDriver = QDriver("driver")
        val predicate  = qDriver.name.startsWith("Z")
        val drivers = driverRepo.findAll(predicate)

        return ResponseEntity.ok(drivers)
    }

    @RequestMapping(method = [RequestMethod.GET], value = ["/agg"])
    fun aggregation(): ResponseEntity<*> {
        val agg = newAggregation(
                match(Criteria.where("surname").`is`("Smith")),
                group("firstName").count().`as`("count"),
                sort(Sort.Direction.DESC, "count")
        )

        return ResponseEntity.ok(mongoTemplate.aggregate(agg, "driver", Driver::class.java).rawResults)
    }
}