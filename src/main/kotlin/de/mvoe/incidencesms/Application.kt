package de.mvoe.incidencesms

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.serialization.responseObject
import de.mvoe.incidencesms.history.IncidenceHistoryResponse
import de.mvoe.incidencesms.week.IncidenceWeekResponse
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun Double.format(digits: Int) = "%.${digits}f".format(this)

object MainKt {

    private var smsApiKey: String = ""
    private var recipients: List<String> = emptyList()

    private val formatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("MM/dd")
        .withLocale(Locale.GERMANY)
        .withZone(ZoneId.of("UTC"));

    @JvmStatic
    fun main(args: Array<String>) {
        args.apply {
            smsApiKey = firstOrNull() ?: throw RuntimeException("No sms77 api key provided")
            recipients = getOrNull(1)?.split(",")?.map { it.trim() }
                ?: throw RuntimeException("No recipient phone number provided")
        }
        val incidenceWeek = getIncidenceWeek()
        val incidenceHistory = getIncidenceHistory()
        val message = makeMessage(incidenceWeek, incidenceHistory)
        recipients.forEach { sendSMS(it, message) }
    }

    private fun makeMessage(week: IncidenceWeekResponse, history: IncidenceHistoryResponse): String {
        history.data.`08115`.apply {
            val sevenDaysIncidence = week.data.`08115`.weekIncidence.format(1);
            val fiveDaysListed = this.history
                .sortedByDescending { it.date }
                .map { Pair<String, Double>(formatter.format(Instant.parse(it.date)), it.weekIncidence) }
                .joinToString("\n") { "${it.first} ${it.second.format(1)}" }
            return "Inzidenz-Report\n7-Tage: $sevenDaysIncidence\n$fiveDaysListed"
        }
    }

    private fun getIncidenceWeek(): IncidenceWeekResponse {
        val url = "https://api.corona-zahlen.org/districts/08115"
        val (_, _, result) = url
            .httpGet()
            .responseObject<IncidenceWeekResponse>()
        return result.get()
    }

    private fun getIncidenceHistory(): IncidenceHistoryResponse {
        val url = "https://api.corona-zahlen.org/districts/08115/history/incidence/5"
        val (_, _, result) = url
            .httpGet()
            .responseObject<IncidenceHistoryResponse>()
        return result.get()
    }

    private fun sendSMS(recipient: String, text: String) {
        println("TO: $recipient")
        println("MSG: $text")
        val url = "https://gateway.sms77.io/api/sms"
        url.httpPost(
            listOf(
                Pair("to", recipient),
                Pair("text", text)
            )
        )
            .header(mapOf("Authorization" to "Basic $smsApiKey"))
            .response()
    }

}
