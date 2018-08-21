package com.giles.android.pagedlistwithnetwork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.giles.android.pagedlistwithnetwork.di.Injectable;
import com.giles.android.pagedlistwithnetwork.ui.user.UserFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by ggshao on 2018/5/28.
 */

public class MainActivity extends AppCompatActivity implements Injectable, HasSupportFragmentInjector {


    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        String tag = UserFragment.TAG;
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            UserFragment fragment = UserFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, tag).commit();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
