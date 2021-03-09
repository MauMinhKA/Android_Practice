package com.example.recyclerview_mvvm_livedata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private var mListUser: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindUser(user: User) {
            itemView.img_avatar.setImageResource(user.imageAvatar)
            itemView.tv_name.text = user.name
            itemView.tv_description.text = user.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindUser(mListUser[position])

    }

    override fun getItemCount(): Int {
        return mListUser.size

    }


}