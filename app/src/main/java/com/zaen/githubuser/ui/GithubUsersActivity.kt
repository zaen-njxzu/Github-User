package com.zaen.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.zaen.githubuser.R
import com.zaen.githubuser.repository.UsersRepository
import kotlinx.android.synthetic.main.activity_github_users.*

class GithubUsersActivity : AppCompatActivity() {

    lateinit var usersViewModel: GithubUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_users)

        val repository = UsersRepository()
        val githubUsersViewModelProviderFactory = GithubUsersViewModelProviderFactory(application, repository)
        usersViewModel = ViewModelProvider(this, githubUsersViewModelProviderFactory).get(GithubUsersViewModel::class.java)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_favorite -> {
                    Toast.makeText(this, "Action favorite pressed!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_settings -> {
                    Toast.makeText(this, "Action setting pressed!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

}
