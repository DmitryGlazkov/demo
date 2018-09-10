package com.example.demo.controller

import com.example.demo.model.Driver
import com.example.demo.model.QDriver
import com.example.demo.repo.DriverRepo
import com.example.demo.repo.ReactiveDriverRepo
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.domain.Sort
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity.ok
import reactor.core.publisher.Mono
import org.springframework.web.reactive.function.server.router
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.toMono

@RestController
class DriverController {

    @Autowired
    private lateinit var driverRepo: DriverRepo

//    @Autowired
//    private lateinit var reactiveDriverRepo: ReactiveDriverRepo

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var rabbit: RabbitTemplate

    @Autowired
    private lateinit var elasticTemplate: ElasticsearchTemplate

//    @RequestMapping(method = [RequestMethod.GET], value = ["/drivers"])
//    fun findAll(): ResponseEntity<*> = ResponseEntity.ok(driverRepo.findAll())
//
//    @RequestMapping(method = [RequestMethod.GET], value = ["/nameZ"])
//    fun findDrivers(): ResponseEntity<*> {
//        val qDriver = QDriver("driver")
//        val predicate  = qDriver.name.startsWith("Z")
//        val drivers = driverRepo.findAll(predicate)
//
//        return ResponseEntity.ok(drivers)
//    }

    @RequestMapping(method = [RequestMethod.GET], value = ["/agg"])
    fun aggregation(): ResponseEntity<*> {
        val agg = newAggregation(
                match(Criteria.where("surname").`is`("Smith")),
                group("firstName").count().`as`("count"),
                sort(Sort.Direction.DESC, "count")
        )

        return ResponseEntity.ok(mongoTemplate.aggregate(agg, "driver", Driver::class.java).rawResults)
    }

//    fun findBySurname(req: ServerRequest): Mono<ServerResponse> {
//        val driver = reactiveDriverRepo.findBySurname(req.pathVariable("surname"))
//        return ServerResponse.ok().body(driver, Driver::class.java)
//    }

    fun getDriver(request: ServerRequest): Mono<ServerResponse> {
        println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZ")
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
//                .body(reactiveDriverRepo.findBySurname(request.pathVariable("surname")), Driver::class.java)
                .body(driverRepo.findBySurname(request.pathVariable("surname")).toMono(), Driver::class.java)
                .switchIfEmpty(ServerResponse.notFound().build())
    }
}