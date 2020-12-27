package com.zaen.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaen.githubuser.R
import com.zaen.githubuser.models.UserInfo
import kotlinx.android.synthetic.main.item_user_info_preview.view.*

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersInfoViewHolder>() {

    inner class UsersInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<UserInfo>() {
        override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersInfoViewHolder {
        return UsersInfoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user_info_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UsersInfoViewHolder, position: Int) {
        val userInfo = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(userInfo.profile_image_url).into(ivProfileImage)
            tvUsername.text = userInfo.username
            tvGithubLink.text = userInfo.user_github_url
            setOnClickListener {
                onItemClickListener?.let {
                    it(userInfo)
                }
            }
        }
    }

    private var onItemClickListener: ((UserInfo) -> Unit)? = null

    fun setOnItemClickListener(listener: (UserInfo) -> Unit) {
        onItemClickListener = listener
    }
}