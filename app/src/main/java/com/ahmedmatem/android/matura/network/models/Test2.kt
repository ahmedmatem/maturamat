package com.ahmedmatem.android.matura.network.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.Date

@Keep
@Parcelize
@Entity(tableName = "test2")
data class Test2(
    @PrimaryKey
    @Json(name = "Id")
    val id: String,
    @ColumnInfo(name = "first_problem_id")
    @Json(name = "FirstProblemId")
    val firstProblemId: String,
    @ColumnInfo(name = "second_problem_id")
    @Json(name = "SecondProblemId")
    val secondProblemId: String,
    @ColumnInfo(name = "third_problem_id")
    @Json(name = "ThirdProblemId")
    val thirdProblemId: String,
    @ColumnInfo(name = "created_on")
    @Json(name = "CreatedOn")
    val createdOn: Date?,

    /**
     *  Next fields will be ignored in mapping.
     *  Each column contains the nullable string of photo names separated by ','.
     */
    @Transient
    @ColumnInfo(name = "solution_1")
    val firstSolutions: String? = null,
    @Transient
    @ColumnInfo(name = "solution_2")
    val secondSolutions: String? = null,
    @Transient
    @ColumnInfo(name = "solution_3")
    val thirdSolutions: String? = null
) : Parcelable

fun Test2.create() : Test2 = Test2(
    id = this.id,
    firstProblemId = this.firstProblemId,
    secondProblemId = this.secondProblemId,
    thirdProblemId = this.thirdProblemId,
    createdOn = Calendar.getInstance().time
)
