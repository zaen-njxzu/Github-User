package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zaen.githubuser.R
import com.zaen.githubuser.adapter.UsersAdapter
import com.zaen.githubuser.ui.GithubUsersActivity
import com.zaen.githubuser.ui.GithubUsersViewModel
import kotlinx.android.synthetic.main.activity_github_users.*
import kotlinx.android.synthetic.main.fragment_favorite_users.*

class FavoriteUsersFragment : Fragment(R.layout.fragment_favorite_users) {

    private lateinit var usersViewModel: GithubUsersViewModel
    private lateinit var usersInfoAdapter: UsersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersViewModel = (activity as GithubUsersActivity).usersViewModel

        setupSwipeActionDeleteUser(view)
        setupTitleTopbar()
        setupRecycleView()
        setupOnClickUserDetailsListener()
        observeAndUpdateListOfSavedUsers()
    }

    private fun setupSwipeActionDeleteUser(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favoriteUser = usersInfoAdapter.differ.currentList[position]
                usersViewModel.deleteFavoriteUser(favoriteUser)
                Snackbar.make(view, "Successfully deleted user", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        usersViewModel.saveFavoriteUser(favoriteUser)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvUsers)
        }
    }

    private fun observeAndUpdateListOfSavedUsers() {
        usersViewModel.getFavoriteUsers().observe(viewLifecycleOwner, Observer { usersInfo ->
            usersInfoAdapter.differ.submitList(usersInfo)
        })
    }

    private fun setupOnClickUserDetailsListener() {
        usersInfoAdapter.setOnItemClickListener {
            usersViewModel.followersUserData.postValue(null)
            usersViewModel.followingsUserData.postValue(null)

            val bundle = Bundle().apply {
                putSerializable("user_info", it)
            }
            findNavController().navigate(
                R.id.action_savedUsersFragment_to_githubDetailUserFragment,
                bundle
            )
        }
    }

    private fun setupRecycleView() {
        usersInfoAdapter = UsersAdapter()
        rvUsers.apply {
            adapter = usersInfoAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupTitleTopbar() {
        activity?.topAppBar?.title = "Favorite Users"
    }
}