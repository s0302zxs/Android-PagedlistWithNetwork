package com.giles.android.pagedlistwithnetwork.ui.userDetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giles.android.pagedlistwithnetwork.R;
import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.Status;
import com.giles.android.pagedlistwithnetwork.data.model.UserDetail;
import com.giles.android.pagedlistwithnetwork.databinding.UserDetailFragmentBinding;
import com.giles.android.pagedlistwithnetwork.di.Injectable;
import com.giles.android.pagedlistwithnetwork.ui.common.SharedViewModel;
import com.giles.android.pagedlistwithnetwork.viewModel.PagedlistWithNetworkViewModelFactory;

import javax.inject.Inject;

import timber.log.Timber;

public class UserDetailFragment extends Fragment implements Injectable {
    public static final String TAG = "UserDetailFragment";

    @Inject
    PagedlistWithNetworkViewModelFactory factory;

    private UserDetailFragmentBinding mBinding;
    private UserDetailViewModel userDetailViewModel;
    private SharedViewModel sharedViewModel;

    public static UserDetailFragment newInstance() {
        UserDetailFragment userDetailFragment = new UserDetailFragment();
        return userDetailFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = UserDetailFragmentBinding.inflate(inflater, container, false);
        mBinding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userDetailViewModel = ViewModelProviders.of(getActivity(), factory).get(UserDetailViewModel.class);
        sharedViewModel = ViewModelProviders.of(getActivity(), factory).get(SharedViewModel.class);

        sharedViewModel.getUsername().observe(this, item ->{
            Timber.d("item = " + item);
            userDetailViewModel.getUserDetail(item);
            //loading

        });

        userDetailViewModel.getUserDetailLiveData().observe(this, it->{
            Timber.d("getUserDetailLiveData it = " + it);
            //update UI
            updateUserDetailUi(it);

        });

        userDetailViewModel.getInitialState().observe(this, networkState ->{
            Timber.d("getInitialState networkState = " + networkState);
            setInitialLoadingState(networkState);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
    }

    private void updateUserDetailUi(UserDetail userDetail){
        if(userDetail != null){
            mBinding.setUserDetail(userDetail);
            mBinding.layoutLocation.imgFunction.setImageResource(R.mipmap.icon_location);
            mBinding.layoutLocation.txtTitle.setText(userDetail.getLocation());
            mBinding.layoutLink.imgFunction.setImageResource(R.mipmap.icon_link);
            mBinding.layoutLink.txtTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.link_blue));
            mBinding.layoutLink.txtTitle.setText(userDetail.getBlog());
        }
    }

    private void setInitialLoadingState(NetworkState networkState){
        Timber.d("setInitialLoadingState networkState = " + networkState);
        mBinding.layoutAll.setVisibility(networkState.getStatus() == Status.SUCCESS ? View.VISIBLE : View.GONE);
        mBinding.progressBar.retryLoadingButton.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        mBinding.progressBar.retryLoadingButton.setOnClickListener(it -> userDetailViewModel.retry());
        mBinding.progressBar.loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);
        mBinding.progressBar.errorMessageTextView.setVisibility(networkState.getMsg() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMsg() != null) {
            mBinding.progressBar.errorMessageTextView.setText(networkState.getMsg());
        }

    }
}
