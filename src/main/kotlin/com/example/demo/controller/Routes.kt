package com.example.demo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

//@Component
//@Configuration
//class Routes {

//    @Autowired
//    private lateinit var driverController: DriverController

    fun router(driverController: DriverController) = router {
        accept(APPLICATION_JSON).nest {
            println("accept(APPLICATION_JSON).nest {")
            "/api".nest {
                println(".nest {111")
                "/driver".nest {
                    println(".nest {222")
                    GET("/{surname}", driverController::getDriver)
                    println(".nest {333")
                }
            }
        }
    }
//}