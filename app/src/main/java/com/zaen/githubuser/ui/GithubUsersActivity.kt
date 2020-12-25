package com.zaen.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zaen.githubuser.R
import com.zaen.githubuser.db.UserInfoDatabase
import com.zaen.githubuser.repository.UsersRepository
import com.zaen.githubuser.util.AlarmReceiver
import com.zaen.githubuser.util.Constants.Companion.PREFS_NAME
import kotlinx.android.synthetic.main.activity_github_users.*


class GithubUsersActivity : AppCompatActivity() {

    lateinit var usersViewModel: GithubUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_users)

        setSupportActionBar(topAppBar)
        runOnetimeCodeExecution()

        val repository = UsersRepository(UserInfoDatabase(this))
        val githubUsersViewModelProviderFactory = GithubUsersViewModelProviderFactory(application, repository)
        usersViewModel = ViewModelProvider(this, githubUsersViewModelProviderFactory).get(GithubUsersViewModel::class.java)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_settings -> {
                    Toast.makeText(this, "Action setting pressed!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        super.onCreateOptionsMenu(menu)
        return true
    }

    fun runOnetimeCodeExecution() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.getBoolean("firstTime", false)) {

            val alarmReceiver = AlarmReceiver()
            alarmReceiver.setRepeatingAlarmOn9AM(this)

            val editor = prefs.edit()
            editor.putBoolean("firstTime", true)
            editor.commit()
        }
    }

}
