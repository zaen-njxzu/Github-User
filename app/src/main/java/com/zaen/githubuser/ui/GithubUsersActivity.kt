package com.zaen.githubuser.ui

import android.os.Bundle

import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.zaen.githubuser.R
import com.zaen.githubuser.db.UserInfoDatabase
import com.zaen.githubuser.repository.UsersRepository
import com.zaen.githubuser.receiver.AlarmReceiver
import com.zaen.githubuser.util.UserPreference
import kotlinx.android.synthetic.main.activity_github_users.*

class GithubUsersActivity : AppCompatActivity() {

    lateinit var usersViewModel: GithubUsersViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_users)

        setSupportActionBar(topAppBar)
        setupAppbarConfiguration()
        runOnetimeCodeExecution()
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = UsersRepository(UserInfoDatabase(this))
        val githubUsersViewModelProviderFactory = GithubUsersViewModelProviderFactory(application, repository)
        usersViewModel = ViewModelProvider(this, githubUsersViewModelProviderFactory).get(GithubUsersViewModel::class.java)
    }

    private fun setupAppbarConfiguration() {
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.action_favorite, R.id.action_settings))

        val navController = findNavController(R.id.nav_host_github_users_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        topAppBar.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val navController = findNavController(R.id.nav_host_github_users_fragment)
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_settings -> {
                    if(navController.currentDestination?.id != R.id.settingsFragment) navController.navigate(R.id.settingsFragment, null, navOptions.build())
                    true
                }
                R.id.action_favorite -> {
                    navController.navigate(R.id.savedUsersFragment, null, navOptions.build())
                    true
                }
                else -> false
            }
        }

        menuInflater.inflate(R.menu.top_app_bar, menu)

        super.onCreateOptionsMenu(menu)
        return true
    }

    fun runOnetimeCodeExecution() {
        val userPreference = UserPreference(this)
        userPreference.apply {
            if(isFirstOpenApp) {
                val alarmReceiver = AlarmReceiver()
                alarmReceiver.setRepeatingAlarmOn9AM(this@GithubUsersActivity)
                isAlarmActive = true

                isFirstOpenApp = false
            }
        }
    }

}
