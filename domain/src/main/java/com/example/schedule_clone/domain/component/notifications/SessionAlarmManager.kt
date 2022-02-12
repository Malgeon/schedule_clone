package com.example.schedule_clone.domain.component.notifications

import android.app.AlarmManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class SessionAlarmManager @Inject constructor(@ApplicationContext val context: Context){

    private val systemAlarmManager: AlarmManager? = context.getSystemService()
}