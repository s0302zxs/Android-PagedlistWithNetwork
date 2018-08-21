package com.giles.android.pagedlistwithnetwork.ui.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.giles.android.pagedlistwithnetwork.data.RetryCallback;
import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.Status;
import com.giles.android.pagedlistwithnetwork.databinding.NetworkStateItemBinding;

import timber.log.Timber;

public class NetworkStateViewHolder extends RecyclerView.ViewHolder {

//    public NetworkStateViewHolder(View itemView) {
//        super(itemView);
//    }

    private final NetworkStateItemBinding mBinding;

    public NetworkStateViewHolder(NetworkStateItemBinding binding, RetryCallback retryCallback) {
        super(binding.getRoot());
        mBinding = binding;

        mBinding.retryLoadingButton.setOnClickListener(view -> retryCallback.retry());
    }

    public void bind(NetworkState networkState) {
        Timber.d("bind networkState = " + networkState);
        mBinding.setNetworkState(networkState);
        mBinding.executePendingBindings();

        mBinding.errorMessageTextView.setVisibility(networkState.getMsg() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMsg() != null) {
            mBinding.errorMessageTextView.setText(networkState.getMsg());
        }

        //loading and retry
        mBinding.retryLoadingButton.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        mBinding.loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);

    }


}
