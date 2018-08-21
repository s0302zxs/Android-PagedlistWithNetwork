package com.giles.android.pagedlistwithnetwork.ui.user;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giles.android.pagedlistwithnetwork.R;
import com.giles.android.pagedlistwithnetwork.data.RetryCallback;
import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.User;
import com.giles.android.pagedlistwithnetwork.databinding.NetworkStateItemBinding;
import com.giles.android.pagedlistwithnetwork.databinding.UserItemBinding;
import com.giles.android.pagedlistwithnetwork.ui.common.NetworkStateViewHolder;

import java.util.Objects;

import timber.log.Timber;

public class UserAdapter extends PagedListAdapter<User, RecyclerView.ViewHolder> {
    private NetworkState networkState;

    private RetryCallback retryCallback;

    public OnUserItemClickListener mItemClickListener;

    UserAdapter(RetryCallback retryCallback){
        super(UserDiffCallback);
        this.retryCallback = retryCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case R.layout.user_item:
                UserItemBinding binding = UserItemBinding.inflate(layoutInflater, parent, false);
                return new UserViewHolder(binding);
            case R.layout.network_state_item:
//                return NetworkStateViewHolder.create(parent, retryCallback);
                NetworkStateItemBinding networkStateItemBinding = NetworkStateItemBinding.inflate(layoutInflater, parent, false);
                return new NetworkStateViewHolder(networkStateItemBinding, retryCallback);
            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.user_item:
                ((UserViewHolder) holder).bind(getItem(position));
                if (mItemClickListener != null) {

                }
                ((UserViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onClick(position, getItem(position));
                    }
                });
                break;
            case R.layout.network_state_item:
                ((NetworkStateViewHolder) holder).bind(networkState);
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        } else {
            return R.layout.user_item;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.Companion.getLOADED();
    }

    private static DiffUtil.ItemCallback<User> UserDiffCallback = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public void setNetworkState(NetworkState newNetworkState) {
        Timber.d("setNetworkState newNetworkState = " + newNetworkState);
        if (getCurrentList() != null) {
            if (getCurrentList().size() != 0) {
                NetworkState previousState = this.networkState;
                boolean hadExtraRow = hasExtraRow();
                this.networkState = newNetworkState;
                boolean hasExtraRow = hasExtraRow();

                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount());
                    } else {
                        notifyItemInserted(super.getItemCount());
                    }
                } else if (hasExtraRow && previousState != newNetworkState) {
                    notifyItemChanged(getItemCount() - 1);
                }
            }
        }
    }

    public void setOnItemClickListener(OnUserItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnUserItemClickListener {
        void onClick(int position, User user);
    }
}
