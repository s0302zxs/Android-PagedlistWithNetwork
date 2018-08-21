package com.giles.android.pagedlistwithnetwork.ui.user;

import android.support.v7.widget.RecyclerView;

import com.giles.android.pagedlistwithnetwork.data.model.User;
import com.giles.android.pagedlistwithnetwork.databinding.UserItemBinding;

public class UserViewHolder extends RecyclerView.ViewHolder{

    private final UserItemBinding mBinding;

    public UserViewHolder(UserItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;

    }

    void bind(User user) {
        mBinding.setUser(user);
        mBinding.executePendingBindings();
    }

}
