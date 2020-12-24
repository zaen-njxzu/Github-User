package com.zaen.githubuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaen.githubuser.R
import com.zaen.githubuser.models.GithubUserInfo
import kotlinx.android.synthetic.main.item_github_user_info_preview.view.*

class GithubUsersAdapter : RecyclerView.Adapter<GithubUsersAdapter.GithubUserInfoViewHolder>() {

    inner class GithubUserInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<GithubUserInfo>() {
        override fun areItemsTheSame(oldItem: GithubUserInfo, newItem: GithubUserInfo): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: GithubUserInfo, newItem: GithubUserInfo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserInfoViewHolder {
        return GithubUserInfoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_github_user_info_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: GithubUserInfoViewHolder, position: Int) {
        val githubUserInfo = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(githubUserInfo.avatar_url).into(ivProfileImage)
            tvUsername.text = githubUserInfo.login
            tvGithubLink.text = githubUserInfo.html_url
            setOnClickListener {
                onItemClickListener?.let {
                    it(githubUserInfo)
                }
            }
        }
    }

    private var onItemClickListener: ((GithubUserInfo) -> Unit)? = null

    fun setOnItemClickListener(listener: (GithubUserInfo) -> Unit) {
        onItemClickListener = listener
    }
}