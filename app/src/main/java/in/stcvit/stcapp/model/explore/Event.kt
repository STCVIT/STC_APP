package `in`.stcvit.stcapp.model.explore

import `in`.stcvit.stcapp.util.formatDate
import `in`.stcvit.stcapp.util.timestampToEpochSeconds
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "EVENTS")
class Event(
    @SerializedName("title")
    val title: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,
) : Serializable {
    init {
        description = description
            .replace("  ", " ")
            .trimIndent()
    }

    @PrimaryKey
    @SerializedName("_id")
    var id: String = ""

    @Ignore
    var status: String? = null

    @Ignore
    var expand: Boolean = false

    @Ignore
    var selected: Boolean = false

    @Ignore
    var reminder: String? = null

    init {
        if (!startDate.matches(Regex("^\\d+\$"))) {
            val startDate1 = Date(timestampToEpochSeconds(startDate))
            val endDate1 = Date(timestampToEpochSeconds(endDate))
            status = if (startDate1.after(Date()))
                "UPCOMING"
            else if (startDate1.before(Date()) && endDate1.after(Date()))
                "ONGOING"
            else
                "COMPLETED"

            if (startDate1.after(Date())) {
                val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                reminder =
                    "${formatDate(startDate1)} @ ${sdf.format(startDate1)} - ${
                        sdf.format(
                            endDate1
                        )
                    }"
            }
        }
    }

    override fun toString(): String = "$id, $title, $description, $startDate, $endDate, $link"

}