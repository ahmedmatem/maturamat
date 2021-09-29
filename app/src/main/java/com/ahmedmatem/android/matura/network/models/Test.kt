package com.ahmedmatem.android.matura.network.models

import androidx.annotation.Keep
import com.ahmedmatem.android.matura.local.entities.TestEntity
import com.squareup.moshi.Json
import java.util.*
import kotlin.collections.ArrayList

@Keep
data class Test(
    @Json(name = "Id") val id: String,
    @Json(name = "ProblemIds") val problemIds: String,
    @Json(name = "ResultInPercent") val resultInPercent: String,
    @Json(name = "IsSaved") val isSaved: Boolean,
    @Json(name = "CreatedOn") val createdOn: Date,
    @Json(name = "ModifiedOn") val modifiedOn: Date,
    @Json(name = "AnswersNumber") val answersNumber: String,
    @Json(name = "CorrectAnswersNumber") val correctAnswersNumber: String,
    @Json(name = "MillisUntilFinished") val millisUntilFinished: Long,
    @Json(name = "State") val state: Int,
    @Json(name = "HasTimer") val hasTimer: Boolean
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

fun Test.toDatabaseModel(isGuest: Boolean) = TestEntity(
    id,
    isGuest = isGuest,
    resultInPercent,
    isSaved,
    createdOn,
    modifiedOn,
    answersNumber,
    correctAnswersNumber,
    millisUntilFinished,
    state,
    hasTimer
)

fun List<Test>.toDatabaseModel(isGuest: Boolean): ArrayList<TestEntity> {
    val databaseModelList = ArrayList<TestEntity>()
    forEach {
        databaseModelList.add(it.toDatabaseModel(isGuest))
    }
    return databaseModelList
}
