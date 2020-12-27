package com.zaen.consumerghapp.ui

import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaen.consumerghapp.R
import com.zaen.githubuser.consumerapp.adapter.UsersAdapter
import com.zaen.githubuser.consumerapp.db.util.DatabaseContract
import com.zaen.githubuser.consumerapp.db.util.DatabaseContract.AUTHORITY
import com.zaen.githubuser.consumerapp.db.util.DatabaseContract.SCHEME
import com.zaen.githubuser.consumerapp.db.util.DatabaseContract.UserInfoColumns.Companion.TABLE_NAME
import com.zaen.githubuser.consumerapp.helper.MappingHelper
import com.zaen.githubuser.consumerapp.models.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val userData: MutableLiveData<List<UserInfo>> = MutableLiveData()
    private lateinit var usersInfoAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Consumer Users"

        setupRecycleView()
        observeUserData()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUsersAsync()
            }
        }
        contentResolver.registerContentObserver(DatabaseContract.UserInfoColumns.CONTENT_URI, true, myObserver)

        loadUsersAsync()
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {

            val deferredUsers = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(DatabaseContract.UserInfoColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToUsersInfo(cursor)
            }
            val usersInfo = deferredUsers.await()

            userData.postValue(usersInfo)

        }
    }

    private fun observeUserData() {
        userData.observe(this, Observer {
            usersInfoAdapter.differ.submitList(it)
        })
    }

    private fun setupRecycleView() {
        usersInfoAdapter = UsersAdapter()
        rvUsers.apply {
            adapter = usersInfoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}