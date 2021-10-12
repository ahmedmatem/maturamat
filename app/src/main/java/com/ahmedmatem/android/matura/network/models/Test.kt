package com.ahmedmatem.android.matura.network.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ahmedmatem.android.matura.infrastructure.toUiFormat
import com.squareup.moshi.Json
import java.util.*

@Keep
@Entity(tableName = "test_table")
data class Test(
    @PrimaryKey
    @Json(name = "Id") val id: String,

    @ColumnInfo(name = "problem_ids")
    @Json(name = "ProblemIds") val problemIds: String,

    @ColumnInfo(name = "result_in_percent")
    @Json(name = "ResultInPercent") val resultInPercent: String,

    @ColumnInfo(name = "is_saved")
    @Json(name = "IsSaved") val isSaved: Boolean,

    @ColumnInfo(name = "created_on")
    @Json(name = "CreatedOn") val createdOn: Date,

    @ColumnInfo(name = "modified_on")
    @Json(name = "ModifiedOn") val modifiedOn: Date,

    @ColumnInfo(name = "answer_number")
    @Json(name = "AnswersNumber") val answersNumber: String,

    @ColumnInfo(name = "correct_answer_number")
    @Json(name = "CorrectAnswersNumber") val correctAnswersNumber: String,

    @ColumnInfo(name = "millis_until_finished")
    @Json(name = "MillisUntilFinished") val millisUntilFinished: Long,

    @Json(name = "State") val state: Int,

    @ColumnInfo(name = "has_timer")
    @Json(name = "HasTimer") val hasTimer: Boolean,

    @ColumnInfo(name = "username")
    @Transient var username: String? = null // default value required
) {
    @Ignore
    @Transient
    val title = createdOn.toUiFormat(UiDateFormat)

    @Ignore
    @Transient
    val subtitle = "$correctAnswersNumber/$answersNumber, $resultInPercent%"

    companion object {
        const val UiDateFormat: String = "dd/MM/yyyy, HH:mm"
    }
}

fun Test.addUsername(username: String?) {
    this.username = username
}

fun List<Test>.addUsername(username: String?): List<Test> {
    forEach { test ->
        test.username = username
    }
    return this
}
