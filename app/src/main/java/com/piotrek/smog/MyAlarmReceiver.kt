package com.piotrek.smog

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.piotrek.smog.api.ApiService


class MyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        ApiService.startActionFetchApi(context)
    }
}