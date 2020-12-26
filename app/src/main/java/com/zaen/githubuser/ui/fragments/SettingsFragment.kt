package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.zaen.githubuser.R
import com.zaen.githubuser.util.AlarmReceiver
import kotlinx.android.synthetic.main.activity_github_users.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val alarmReceiver = AlarmReceiver()
    private var isAlarmSet = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCurrentSettings()
        setupTitleTopbar()

        switchAlarm.setOnClickListener {
            switchAlarm()
        }

    }

    private fun setupTitleTopbar() {
        activity?.topAppBar?.title = "Settings"
    }

    private fun loadCurrentSettings() {
        context?.apply {
            isAlarmSet = alarmReceiver.isAlarmSet(this, AlarmReceiver.TYPE_REPEATING)

            switchAlarm.isChecked = isAlarmSet
            tvSwitchAlarm.text =  if (isAlarmSet) "ON" else "OFF"
        }
    }

    private fun switchAlarm() {
        isAlarmSet = !isAlarmSet

        switchAlarm.isChecked = isAlarmSet
        tvSwitchAlarm.text =  if (isAlarmSet) "ON" else "OFF"

        context?.apply {
            if(isAlarmSet) alarmReceiver.setRepeatingAlarmOn9AM(this)
            else alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
        }
    }

}