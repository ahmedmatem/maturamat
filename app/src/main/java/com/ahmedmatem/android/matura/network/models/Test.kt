package com.ahmedmatem.android.matura.network.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Keep
@Entity(tableName = "test_table")
data class Test(
    @PrimaryKey @Json(name = "Id") val id: String,
    @ColumnInfo(name = "problem_ids") @Json(name = "ProblemIds") val problemIds: String,
    @ColumnInfo(name = "result_in_percent") @Json(name = "ResultInPercent") val resultInPercent: String,
    @ColumnInfo(name = "is_saved") @Json(name = "IsSaved") val isSaved: Boolean,
    @ColumnInfo(name = "created_on") @Json(name = "CreatedOn") val createdOn: Date,
    @ColumnInfo(name = "modified_on") @Json(name = "ModifiedOn") val modifiedOn: Date,
    @ColumnInfo(name = "answer_number") @Json(name = "AnswersNumber") val answersNumber: String,
    @ColumnInfo(name = "correct_answer_number") @Json(name = "CorrectAnswersNumber") val correctAnswersNumber: String,
    @ColumnInfo(name = "millis_until_finished") @Json(name = "MillisUntilFinished") val millisUntilFinished: Long,
    @Json(name = "State") val state: Int,
    @ColumnInfo(name = "has_timer") @Json(name = "HasTimer") val hasTimer: Boolean,
    @ColumnInfo(name = "is_guest") @Transient val isGuest: Boolean = true
) {

    override fun toString(): String {
        return "Test{" +
                "id='" + id + '\'' +
                ", problemIds='" + problemIds + '\'' +
                ", resultInPercent='" + resultInPercent + '\'' +
                ", isSaved=" + isSaved +
                ", createdOn=" + createdOn +
                ", modifiedOn=" + modifiedOn +
                ", answersNumber='" + answersNumber + '\'' +
                ", correctAnswersNumber='" + correctAnswersNumber + '\'' +
                ", millisUntilFinished=" + millisUntilFinished +
                ", state=" + state +
                ", hasTimer=" + hasTimer +
                '}';
    }
}
