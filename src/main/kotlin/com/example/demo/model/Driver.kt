package com.example.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Document(collection = "driver")
data class Driver(
        @Id
        val id: String?,
        val uuid: UUID?,
        val uuidString: String?,
        val name: String?,
        val surname: String?,
        val firstName: String?,
        val shortCode: String?,
        val gender: String?,
        val deathDate: LocalDate?,
        val birthDate: LocalDate?
)