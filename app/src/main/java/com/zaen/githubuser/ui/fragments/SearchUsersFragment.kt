package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaen.githubuser.R
import com.zaen.githubuser.adapters.UsersAdapter
import com.zaen.githubuser.ui.GithubUsersActivity
import com.zaen.githubuser.ui.GithubUsersViewModel
import com.zaen.githubuser.util.Constants.Companion.QUERY_PAGE_SIZE
import com.zaen.githubuser.util.Constants.Companion.SEARCH_GITHUB_USERS_TIME_DELAY
import com.zaen.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_search_users.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchUsersFragment : Fragment(R.layout.fragment_search_users) {

    lateinit private var usersViewModel: GithubUsersViewModel
    lateinit private var usersInfoAdapter: UsersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersViewModel = (activity as GithubUsersActivity).usersViewModel

        setupRecycleView()
        setupOnClickUserDetailsListener()
        setupUsernameListenerWithFetchData()
        observeAndUpdateListOfUsers()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_favorite).setVisible(true)
    }

    private fun observeAndUpdateListOfUsers() {
        usersViewModel.searchUsers.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { usersResponse ->
                        usersInfoAdapter.differ.submitList(usersResponse.users_info.toList())
                        val totalPages = usersResponse.total_count / QUERY_PAGE_SIZE + 2
                        isLastPage = usersViewModel.searchUsersPage == totalPages
                        if(isLastPage) {
                            rvUsers.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupUsernameListenerWithFetchData() {
        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_GITHUB_USERS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        usersViewModel.searchUsers(editable.toString(), false)
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScolling

            if(shouldPaginate) {
                usersViewModel.searchUsers(etSearch.text.toString(), true)
                isScolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScolling = true
            }
        }
    }

    private fun setupOnClickUserDetailsListener() {
        usersInfoAdapter.setOnItemClickListener {
            usersViewModel.followersUserData.postValue(null)
            usersViewModel.followingsUserData.postValue(null)

            val bundle = Bundle().apply {
                putSerializable("user_info", it)
            }
            findNavController().navigate(
                R.id.action_searchGithubUsersFragment_to_githubDetailUserFragment,
                bundle
            )
        }
    }

    private fun setupRecycleView() {
        usersInfoAdapter = UsersAdapter()
        rvUsers.apply {
            adapter = usersInfoAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchUsersFragment.scrollListener)
        }
    }

}