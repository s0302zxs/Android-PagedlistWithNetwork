package com.giles.android.pagedlistwithnetwork.ui.common;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.giles.android.pagedlistwithnetwork.R;

public class CommonBindings {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("imageUrl")
    public static void bindImage(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }
}
