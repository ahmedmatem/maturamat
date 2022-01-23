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

    fun createStopNotice(millis: Long): NoticeData {
        return NoticeData(
            context.getString(R.string.test_stop_title),
            context.getString(
                R.string.test_left_time,
                TimeConverter.from(millis).toMessageString()
            ),
            context.getString(R.string.test_resume_btn),
            null,
            null,
            NoticeDialogTag.STOP.tag
        )
    }

    fun createCancelNotice(): NoticeData {
        return NoticeData(
            context.getString(R.string.test_cancel_title),
            context.getString(R.string.test_cancel_message),
            context.getString(R.string.yes_btn),
            context.getString(R.string.no_btn),
            null,
            NoticeDialogTag.CANCEL.tag
        )
    }

    fun createCheckNotice(): NoticeData {
        return NoticeData(
            null,
            context.getString(R.string.test_check_message),
            context.getString(R.string.check_btn),
            null,
            context.getString(R.string.cancel_btn),
            NoticeDialogTag.CHECK.tag
        )
    }

    fun createFinishNotice(): NoticeData {
        return NoticeData(
            context.getString(R.string.test_time_finish_title),
            context.getString(R.string.test_time_finish_message),
            context.getString(R.string.check_result_btn),
            null,
            null,
            NoticeDialogTag.FINISH.tag
        )
    }
}