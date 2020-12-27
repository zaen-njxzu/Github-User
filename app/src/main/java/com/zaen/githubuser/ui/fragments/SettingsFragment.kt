package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.zaen.githubuser.R
import com.zaen.githubuser.receiver.AlarmReceiver
import com.zaen.githubuser.util.UserPreference
import kotlinx.android.synthetic.main.activity_github_users.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val alarmReceiver = AlarmReceiver()
    private lateinit var userPreference: UserPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserPreference()
        loadCurrentSettings()
        setupTitleTopbar()
        setupOnClickListener()
    }

    private fun setupOnClickListener() {
        switchAlarm.setOnClickListener {
            switchAlarm()
        }
    }

    private fun initUserPreference() {
        context?.apply {
            userPreference = UserPreference(this)
        }
    }

    private fun setupTitleTopbar() {
        activity?.topAppBar?.title = "Settings"
    }

    private fun loadCurrentSettings() {
        context?.apply {
            val isAlarmActive = userPreference.isAlarmActive

            switchAlarm.isChecked = isAlarmActive
            tvSwitchAlarm.text =  if (isAlarmActive) "ON" else "OFF"
        }
    }

    private fun switchAlarm() {
        val isAlarmActive = !userPreference.isAlarmActive
        userPreference.isAlarmActive = isAlarmActive

        switchAlarm.isChecked = isAlarmActive
        tvSwitchAlarm.text =  if (isAlarmActive) "ON" else "OFF"

        context?.apply {
            if(isAlarmActive) alarmReceiver.setRepeatingAlarmOn9AM(this)
            else alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
        }
    }

}