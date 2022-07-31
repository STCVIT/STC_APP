package `in`.stcvit.stcapp.model.resource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "DETAILS")
class Detail(
    @SerializedName("description")
    var description: String,

    @SerializedName("domain")
    val domain: String,

    @SerializedName("expectation")
    val expectation: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
) {
    init {
        description = description
            .replace("  ", " ")
            .trimIndent()
    }

    override fun toString(): String = "$id, $domain, $description, $expectation"

}