package com.ahmedmatem.android.matura.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmedmatem.android.matura.network.models.Test
import java.util.*

@Entity(tableName = "test_table")
data class TestEntity(
    @PrimaryKey val id: String,
//    @ColumnInfo(name = "problem_ids")  val problemIds: String,
    @ColumnInfo(name = "is_guest") val isGuest: Boolean,
    @ColumnInfo(name = "result_in_percent") val resultInPercent: String,
    @ColumnInfo(name = "is_saved") val isSaved: Boolean,
    @ColumnInfo(name = "created_on") val createdOn: Date,
    @ColumnInfo(name = "modified_on") val modifiedOn: Date,
    @ColumnInfo(name = "answers_number") val answersNumber: String,
    @ColumnInfo(name = "problem_ids") val correctAnswersNumber: String,
    @ColumnInfo(name = "millis_till_finish") val millisUntilFinished: Long,
    @ColumnInfo(name = "state") val state: Int,
    @ColumnInfo(name = "has_timer") val hasTimer: Boolean
)

fun TestEntity.toDomainModel() = Test(
    id,
    problemIds = "",
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

fun List<TestEntity>.toDomainModel(): ArrayList<Test> {
    val domainModelList = ArrayList<Test>()
    forEach {
        domainModelList.add(it.toDomainModel())
    }
    return domainModelList
}