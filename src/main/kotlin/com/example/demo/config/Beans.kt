package com.example.demo.config

import com.example.demo.controller.router
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.EntityMapper
import java.net.InetAddress


const val RABBIT_HOST = "localhost"
const val RABBIT_PORT = "5672"

const val ELASTIC_HOST = "localhost"
const val ELASTIC_PORT = "9300"
const val ELASTIC_CLUSTER = "mss-cluster"

const val MONGO_DB_NAME = "mss"

fun beans() = beans {

    bean("rabbitTemplate") {
        val connectionFactory = CachingConnectionFactory().apply {
            host = RABBIT_HOST
            port = RABBIT_PORT.toInt()
            println("******* RABBIT HOST = $RABBIT_HOST *******")
            println("******* RABBIT PORT = $RABBIT_PORT *******")
        }
        RabbitTemplate(connectionFactory)
    }

    bean("elasticTemplate") {
        val client = PreBuiltTransportClient(Settings.builder().put("cluster.name", ELASTIC_CLUSTER).build())
                .apply {
                    println("******* ELASTIC HOST: $ELASTIC_HOST *******")
                    println("******* ELASTIC PORT: $ELASTIC_PORT *******")
                    println("******* ELASTIC CLUSTER: $ELASTIC_CLUSTER *******")
                }
                .addTransportAddress(InetSocketTransportAddress(InetAddress.getByName(ELASTIC_HOST), ELASTIC_PORT.toInt()))

        ElasticsearchTemplate(client, object : EntityMapper {
            private val mapper = jacksonObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .registerModule(JavaTimeModule())

            override fun <T : Any?> mapToObject(source: String?, clazz: Class<T>?): T {
                return mapper.readValue(source, clazz)
            }
            override fun mapToString(`object`: Any?): String = mapper.writeValueAsString(`object`)
        }).apply { println("******* Register ElasticsearchTemplate with custom EntityMapper *******") }
    }

    bean { router(ref()) }

//    bean("reactiveMongo") {
//        println("******* ReactiveMongoTemplate *******")
//        ReactiveMongoTemplate(MongoClients.create(ConnectionString("mongodb://localhost:27017")), MONGO_DB_NAME)
//    }
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) =
            beans().initialize(context)
}