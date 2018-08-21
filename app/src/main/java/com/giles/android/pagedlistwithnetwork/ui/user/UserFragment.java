package com.giles.android.pagedlistwithnetwork.ui.user;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giles.android.pagedlistwithnetwork.R;
import com.giles.android.pagedlistwithnetwork.data.RetryCallback;
import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.Status;
import com.giles.android.pagedlistwithnetwork.data.model.User;
import com.giles.android.pagedlistwithnetwork.databinding.UserFragmentBinding;
import com.giles.android.pagedlistwithnetwork.di.Injectable;
import com.giles.android.pagedlistwithnetwork.ui.userDetail.UserDetailFragment;
import com.giles.android.pagedlistwithnetwork.ui.common.SharedViewModel;
import com.giles.android.pagedlistwithnetwork.viewModel.PagedlistWithNetworkViewModelFactory;

import javax.inject.Inject;

import timber.log.Timber;

public class UserFragment extends Fragment implements Injectable, RetryCallback {
    public static final String TAG = "UserFragment";

    @Inject
    PagedlistWithNetworkViewModelFactory factory;

    private UserFragmentBinding mBinding;
    private UserViewModel userViewModel;
    private SharedViewModel sharedViewModel;

    private UserAdapter userAdapter = new UserAdapter(this);

    public static UserFragment newInstance() {
        UserFragment userFragment = new UserFragment();
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = UserFragmentBinding.inflate(inflater, container, false);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mBinding.recyclerView.setAdapter(userAdapter);

        setClicks();

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(getActivity(), factory).get(UserViewModel.class);
        sharedViewModel = ViewModelProviders.of(getActivity(), factory).get(SharedViewModel.class);

        userViewModel.getUsers().observe(this, new Observer<PagedList<User>>() {
            @Override
            public void onChanged(@Nullable PagedList<User> users) {
                Timber.d("onChanged users = " + users);
                userViewModel.isLoading.set(false);
                userAdapter.submitList(users);
            }
        });

        userViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState response) {
                Timber.d("onChanged NetworkState response.getStatus() = " + response.getStatus());
                userAdapter.setNetworkState(response);

            }
        });

        initSwipeToRefresh();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setClicks(){
        UserAdapter.OnUserItemClickListener itemClickListener = new UserAdapter.OnUserItemClickListener() {
            @Override
            public void onClick(int position, User user) {
                Timber.d("itemClickListener onClick position = " + position);
                sharedViewModel.setUsername(user.getLogin());
                launchUserDetailFragment();
            }

        };

        userAdapter.setOnItemClickListener(itemClickListener);
    }

    private void initSwipeToRefresh(){
        userViewModel.getRefreshState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                Timber.d("onChanged networkState = " + networkState);
                if (networkState != null) {
                    Timber.d("onChanged userAdapter.getCurrentList() = " + userAdapter.getCurrentList());
                    if (userAdapter.getCurrentList() != null) {
                        if (userAdapter.getCurrentList().size() > 0) {
                            mBinding.userSwipeRefreshLayout.setRefreshing(
                                    networkState.getStatus() == NetworkState.Companion.getLOADING().getStatus());
                            mBinding.progressBar.loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);
                        } else {
                            setInitialLoadingState(networkState);
                        }
                    }else{
                        setInitialLoadingState(networkState);
                    }

                }

            }
        });

        mBinding.userSwipeRefreshLayout.setOnRefreshListener(() -> userViewModel.refresh());
        mBinding.userSwipeRefreshLayout.setEnabled(false);
    }

    private void setInitialLoadingState(NetworkState networkState){
        Timber.d("setInitialLoadingState networkState = " + networkState);
        mBinding.progressBar.errorMessageTextView.setVisibility(networkState.getMsg() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMsg() != null) {
            mBinding.progressBar.errorMessageTextView.setText(networkState.getMsg());
        }

        mBinding.progressBar.loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);
        mBinding.progressBar.retryLoadingButton.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        mBinding.progressBar.retryLoadingButton.setOnClickListener(it -> userViewModel.retry());
        mBinding.userSwipeRefreshLayout.setEnabled(networkState.getStatus() == Status.SUCCESS);
    }

    private void launchUserDetailFragment(){
        Fragment fragment = UserDetailFragment.newInstance();

        FragmentManager fm = getFragmentManager();
        if (fm == null) {
            Timber.d("[launchFragment] FragmentManager is null.");
            return;
        }

        FragmentTransaction ft = fm.beginTransaction();
        if (ft == null) {
            Timber.d( "[launchFragment] FragmentTransaction is null. ");
            return;
        }

        ft.replace(R.id.container, fragment).addToBackStack(UserDetailFragment.TAG).commitAllowingStateLoss();
    }

    @Override
    public void retry() {
        userViewModel.retry();
    }
}
