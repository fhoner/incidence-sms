package de.mvoe.incidencesms.history

import kotlinx.serialization.Serializable

@Serializable
data class IncidenceHistoryResponse(
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
    val history: List<History>,
    val name: String
)

@Serializable
data class History(
    val date: String,
    val weekIncidence: Double
)
