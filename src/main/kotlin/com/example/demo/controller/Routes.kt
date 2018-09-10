package com.example.demo.controller

import org.springframework.web.reactive.function.server.router
import org.springframework.http.MediaType.APPLICATION_JSON


fun router(driverController: DriverController) = router {
    accept(APPLICATION_JSON).nest {
        "/api".nest {
            "/driver".nest {
                GET("/{surname}", driverController::getDriver)
            }
        }
    }
}