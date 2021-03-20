import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun Double.format(digits: Int) = "%.${digits}f".format(this)

object MainKt {

    private val smsApiKey = "xxxxxxxx"

    private val formatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("MM/dd")
        .withLocale(Locale.GERMANY)
        .withZone(ZoneId.of("UTC"));

    @JvmStatic
    fun main(args: Array<String>) {
        val recipients: List<String> = args.firstOrNull()?.split(",")?.map { it.trim() }
            ?: throw RuntimeException("No recipient phone number provided")
        val incidence = getIncidence()
        val message = makeMessage(incidence)
        recipients.forEach { sendSMS(it, message) }
    }

    private fun makeMessage(incidence: IncidenceResponse): String {
        incidence.data.`08115`.apply {
            val sevenDaysIncidence = history
                .map { it.weekIncidence }
                .average().format(1)
            val fiveDaysListed = history.asSequence()
                .sortedByDescending { it.date }
                .map { Pair<String, Double>(formatter.format(Instant.parse(it.date)), it.weekIncidence) }
                .take(5)
                .map { "${it.first} ${it.second.format(1)}" }
                .joinToString("\n")
            return "Inzidenz-Report\n7-Tage: $sevenDaysIncidence\n$fiveDaysListed"
        }
    }

    /** Gets incidence by RKI wrapper api. */
    private fun getIncidence(): IncidenceResponse {
        val url = "https://api.corona-zahlen.org/districts/08115/history/incidence/7"
        val (_, _, result) = url
            .httpGet()
            .responseObject<IncidenceResponse>()
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

@Serializable
data class IncidenceResponse(
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
