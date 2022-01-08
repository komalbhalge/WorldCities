package com.kb.worldcities

import com.kb.worldcities.data.entities.City
import com.kb.worldcities.data.entities.Coordinates

object FakeCityData {
    val cities = mutableListOf(
        City(
            _id = 123,
            name = "Albuquerque",
            country = "US",
            Coordinates(lon = 1.1, lat = 1.2)
        ), City(
            _id = 1234,
            name = "Putrajaya",
            country = "MY",
            Coordinates(lon = 1.1, lat = 1.2)
        ),
        City(
            _id = 12345,
            name = "Alabama",
            country = "TH",
            Coordinates(lon = 10.4, lat = 3.2)
        ),
        City(
            _id = 12345,
            name = "Cape Town",
            country = "SA",
            Coordinates(lon = 10.4, lat = 3.2)
        )
    )
}