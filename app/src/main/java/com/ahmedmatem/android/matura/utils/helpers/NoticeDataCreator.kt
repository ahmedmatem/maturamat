package com.ahmedmatem.android.matura.utils.helpers

import android.content.Context
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.ui.general.NoticeData
import com.ahmedmatem.android.matura.ui.general.NoticeDialogTag
import com.ahmedmatem.android.matura.utils.TimeConverter

class NoticeDataCreator(val context: Context) {

    fun createStartNotice(millis: Long): NoticeData {
        return NoticeData(
            null,
            context.getString(
                R.string.test_duration_message,
                TimeConverter.from(millis).toMessageString()
            ),
            context.getString(R.string.start_btn),
            context.getString(R.string.cancel_btn),
            null,
            NoticeDialogTag.START.tag
        )
    }
}