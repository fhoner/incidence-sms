package de.mvoe.incidencesms.week

import kotlinx.serialization.Serializable

@Serializable
data class IncidenceWeekResponse(
    val `data`: Data,
    val meta: Meta
)

@Serializable
data class Data(
    val `08115`: X08115
)

@Serializable
data class Meta(
    val contact: String,
    val info: String,
    val lastCheckedForUpdate: String,
    val lastUpdate: String,
    val source: String
)

@Serializable
data class X08115(
    val ags: String,
    val cases: Int,
    val casesPer100k: Double,
    val casesPerWeek: Int,
    val county: String,
    val deaths: Int,
    val deathsPerWeek: Int,
    val delta: Delta,
    val name: String,
    val population: Int,
    val recovered: Int,
    val state: String,
    val stateAbbreviation: String,
    val weekIncidence: Double
)

@Serializable
data class Delta(
    val cases: Int,
    val deaths: Int,
    val recovered: Int
)
