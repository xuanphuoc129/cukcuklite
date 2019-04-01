package com.example.lxphuoc.cukcuklite.splash;

import android.content.Context;

/**
‐ View trong mô hình MVP cho màn hình splash
*
‐ @created_by lxphuoc on 3/23/2019
*/


public interface ISplashContract {

    interface IView{

        void openMainActivity();

    }

    interface IPresenter{

        void onLoadData(Context context);

    }
}