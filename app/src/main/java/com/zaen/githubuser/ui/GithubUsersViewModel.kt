package com.zaen.githubuser.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaen.githubuser.GithubUsersApplication
import com.zaen.githubuser.models.SearchGithubUsersResponse
import com.zaen.githubuser.repository.GithubUsersRepository
import com.zaen.githubuser.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class GithubUsersViewModel(
    val app: Application,
    val githubUsersRepository: GithubUsersRepository
) : AndroidViewModel(app) {

    val searchGithubUsers: MutableLiveData<Resource<SearchGithubUsersResponse>> = MutableLiveData()
    var searchGithubUsersPage = 1
    var searchGithubUsersResponse: SearchGithubUsersResponse? = null

    fun searchGithubUsers(searchQuery: String, isPagination: Boolean) = viewModelScope.launch {
        searchGithubUsers.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = githubUsersRepository.searchGithubUsers(searchQuery, searchGithubUsersPage)
                searchGithubUsers.postValue(handleSearchNewsResponse(response, isPagination))
            } else {
                searchGithubUsers.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchGithubUsers.postValue(Resource.Error("Network Failure"))
                else -> searchGithubUsers.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchNewsResponse(response: Response<SearchGithubUsersResponse>, isPagination: Boolean) : Resource<SearchGithubUsersResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (isPagination == false) {
                    searchGithubUsersPage = 1
                    return Resource.Success(resultResponse)
                }
                else {
                    searchGithubUsersPage++
                    if (searchGithubUsersResponse == null) {
                        searchGithubUsersResponse = resultResponse
                    } else {
                        val oldArticles = searchGithubUsersResponse?.items
                        val newArticles = resultResponse.items
                        oldArticles?.addAll(newArticles)
                    }
                    return Resource.Success(searchGithubUsersResponse ?: resultResponse)
                }
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<GithubUsersApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }
}